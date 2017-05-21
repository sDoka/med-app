package com.baibus.medicalaccreditation.update;

import android.databinding.ObservableInt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.ActivityVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.binding.ObservableString;
import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.baibus.medicalaccreditation.common.db.tables.AnswerTable;
import com.baibus.medicalaccreditation.common.db.tables.AttemptTable;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.baibus.medicalaccreditation.common.network.ApiError;
import com.baibus.medicalaccreditation.common.network.entities.QuestionResponse;
import com.baibus.medicalaccreditation.common.network.entities.SpecializationsResponse;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.baibus.medicalaccreditation.dummy.DummyContent;
import com.baibus.medicalaccreditation.main.MainActivity;
import com.pushtorefresh.storio.StorIOException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.baibus.medicalaccreditation.common.db.tables.SpecializationTable.DELETE_ALL;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.05.2017
 * Time: 22:27
 * To change this template use File | settings | File Templates.
 */
public class UpdateVM extends ActivityVM<UpdateActivity> {
    private final static String TAG = UpdateVM.class.getSimpleName();

    private final static String BUNDLE_STATUS = "mStatus";
    private final static String BUNDLE_ERROR = "error";

    private final static int LOADER_UPDATE = 0;

    public final ObservableString error = new ObservableString();
    private final ObservableInt mStatus = new ObservableInt();
    private final OnPropertyChangedCallback mStatusChanged = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            isShowProgress.set(mStatus.get() != NOTHING);
        }
    };

    private UpdateVM(UpdateActivity activity, @Nullable Bundle savedInstanceState) {
        super(activity, savedInstanceState);

        if (savedInstanceState != null) {
            error.set(savedInstanceState.getString(BUNDLE_ERROR));
            mStatus.set(savedInstanceState.getInt(BUNDLE_STATUS));
        }

        mStatus.addOnPropertyChangedCallback(mStatusChanged);
        resumeLoaders(getStatusFromInt(mStatus.get()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_ERROR, error.get());
        outState.putInt(BUNDLE_STATUS, mStatus.get());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStatus.removeOnPropertyChangedCallback(mStatusChanged);
    }

    public void retryUpdate() {
        restartLoaders(UPDATE_SPECIALIZATIONS);
    }

    private void resumeLoaders(@Status int status) {
        switch (status) {
            case UPDATE_SPECIALIZATIONS:
                loadUpdateSpecializations(false);
                break;
            case UPDATE_QUESTIONS:
                loadUpdateQuestions(false);
                break;
            case NOTHING:
                break;
        }
    }

    private void restartLoaders(@Status int status) {
        switch (status) {
            case UPDATE_SPECIALIZATIONS:
                loadUpdateSpecializations(true);
                break;
            case UPDATE_QUESTIONS:
                loadUpdateQuestions(true);
                break;
            case NOTHING:
                break;
        }
    }

    private void updateSpecializationsSuccess(Boolean result) {
        restartLoaders(UPDATE_QUESTIONS);
    }

    private void onError(Throwable throwable) {
        mStatus.set(NOTHING);
        if (throwable instanceof ApiError) {
            ApiError apiError = (ApiError) throwable;
            switch (apiError.getKind()) {
                case NETWORK:
                    error.set("Ошибка соединения");
                    return;
                case UNAUTHENTICATED:
                case CLIENT:
                case SERVER:
                    error.set(apiError.getMessage());
                    return;
                case UNEXPECTED:
                    throw apiError;
            }
        }
        if (throwable instanceof StorIOException) {
            error.set("Не удалось обновить данные");
            return;
        }
        throw Exceptions.propagate(throwable);
    }

    private void updateQuestionsSuccess(Boolean result) {
        mStatus.set(NOTHING);
        activity.startActivity(MainActivity.getStartIntent());
        activity.finish();
    }

    private void loadUpdateQuestions(boolean restart) {
        activity.addSubscription(
                getObservableUpdateQuestions()
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(activity.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_UPDATE, restart))
                        .doOnSubscribe(() -> {
                            mStatus.set(UPDATE_QUESTIONS);
                            error.set("");
                        })
                        .subscribe(this::updateSpecializationsSuccess,  this::onError)
        );
    }

    private void loadUpdateSpecializations(boolean restart) {
        activity.addSubscription(
                getObservableUpdateSpecializations()
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(activity.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_UPDATE, restart))
                        .doOnSubscribe(() -> {
                            mStatus.set(UPDATE_SPECIALIZATIONS);
                            error.set("");
                        })
                        .subscribe(this::updateQuestionsSuccess, this::onError)
        );
    }

    private static Observable<Boolean> getObservableUpdateQuestions() {
        return Observable
                .defer(() -> {
                    List<Question> questions = ApiModule.getStoreIOSQLite()
                            .get()
                            .listOfObjects(Question.class)
                            .withQuery(QuestionTable.QUERY_ALL)
                            .prepare()
                            .executeAsBlocking();

                    String body;
                    if (questions.isEmpty()) body = DummyContent.QUESTIONS;
                    else body = DummyContent.EMPTY;

                    return ApiModule
                            .getRestApiMock(body)
                            .question();
                })
                .map(new SaveQuestions())
                .doOnEach(result -> ApiModule.shutdownServer())
                .doOnError(throwable -> ApiModule.shutdownServer());
    }


    private static Observable<Boolean> getObservableUpdateSpecializations() {
        return Observable
                .defer(() -> ApiModule
                        .getRestApiMock(DummyContent.SPECIALIZATIONS)
                        .specializations())
                .map(new SaveSpecializations())
                .doOnEach(result -> ApiModule.shutdownServer())
                .doOnError(throwable -> ApiModule.shutdownServer());
    }

    private static class SaveSpecializations implements Func1<SpecializationsResponse, Boolean> {
        @Override
        public Boolean call(SpecializationsResponse specializationsResponse) {

            ApiModule
                    .getStoreIOSQLite()
                    .delete()
                    .byQuery(DELETE_ALL)
                    .prepare()
                    .executeAsBlocking();

            ApiModule
                    .getStoreIOSQLite()
                    .put()
                    .objects(specializationsResponse.data)
                    .prepare()
                    .executeAsBlocking();

            return true;
        }
    }

    private static class SaveQuestions implements Func1<List<QuestionResponse>, Boolean> {
        @Override
        public Boolean call(List<QuestionResponse> questionResponses) {
            StringBuilder idsBuilder = new StringBuilder();
            for (int i = 0, dataSize = questionResponses.size(); i < dataSize; i++) {
                QuestionResponse questionResponse = questionResponses.get(i);
                if (idsBuilder.length() > 0) idsBuilder.append(", ");
                idsBuilder.append(questionResponse.questionId);
            }
            String ids = idsBuilder.toString();

            ApiModule
                    .getStoreIOSQLite()
                    .delete()
                    .byQuery(QuestionTable.deleteIds(ids))
                    .prepare()
                    .executeAsBlocking();

            ApiModule
                    .getStoreIOSQLite()
                    .delete()
                    .byQuery(AttemptTable.deleteIds(ids))
                    .prepare()
                    .executeAsBlocking();

            ApiModule
                    .getStoreIOSQLite()
                    .delete()
                    .byQuery(AnswerTable.deleteIds(ids))
                    .prepare()
                    .executeAsBlocking();

            for (int i = 0, dataSize = questionResponses.size(); i < dataSize; i++) {
                QuestionResponse questionResponse = questionResponses.get(i);

                ApiModule
                        .getStoreIOSQLite()
                        .put()
                        .object(questionResponse.getQuestion())
                        .prepare()
                        .executeAsBlocking();

                ApiModule
                        .getStoreIOSQLite()
                        .put()
                        .objects(questionResponse.answers)
                        .prepare()
                        .executeAsBlocking();
            }

            return true;
        }
    }

    static class FactoryVM implements ActivityVMFactory<UpdateVM, UpdateActivity> {
        @NonNull
        @Override
        public UpdateVM create(UpdateActivity view) {
            return new UpdateVM(view, null);
        }

        @NonNull
        @Override
        public UpdateVM restore(UpdateActivity view, @NonNull Bundle savedInstanceState) {
            return new UpdateVM(view, savedInstanceState);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UPDATE_SPECIALIZATIONS, UPDATE_QUESTIONS, NOTHING})
    private @interface Status{}
    private static final int UPDATE_SPECIALIZATIONS = 0;
    private static final int UPDATE_QUESTIONS = 1;
    private static final int NOTHING = 2;

    private static @Status int getStatusFromInt(int from) {
        switch (from) {
            case UPDATE_SPECIALIZATIONS:
            case UPDATE_QUESTIONS:
            case NOTHING:
                return from;
            default:
                return NOTHING;
        }
    }
}
