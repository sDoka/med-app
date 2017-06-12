package com.baibus.medicalaccreditation.update;

import android.databinding.ObservableInt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.ActivityVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.binding.ObservableString;
import com.baibus.medicalaccreditation.common.db.entities.User;
import com.baibus.medicalaccreditation.common.db.tables.AnswerTable;
import com.baibus.medicalaccreditation.common.db.tables.AttemptTable;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.baibus.medicalaccreditation.common.network.ApiError;
import com.baibus.medicalaccreditation.common.network.entities.QuestionResponse;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.baibus.medicalaccreditation.main.MainActivity;
import com.pushtorefresh.storio.StorIOException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
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
        } else {
            mStatusChanged.onPropertyChanged(mStatus, BR._all);
        }

        mStatus.addOnPropertyChangedCallback(mStatusChanged);
        resumeLoaders(getStatusFromInt(mStatus.get()));
        title.set("Обновление");
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
        restartLoaders(UPDATE_ALL);
    }

    private void resumeLoaders(@Status int status) {
        switch (status) {
            case UPDATE_ALL:
                loadSynchronize(false);
                break;
            case NOTHING:
                break;
        }
    }

    private void restartLoaders(@Status int status) {
        switch (status) {
            case UPDATE_ALL:
                loadSynchronize(true);
                break;
            case NOTHING:
                break;
        }
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
            throwable.printStackTrace();
            return;
        }
        throw Exceptions.propagate(throwable);
    }

    private void updateSuccess(Boolean result) {
        //mStatus.set(NOTHING);
        activity.startActivity(MainActivity.getStartIntent());
        activity.finish();
    }

    private void loadSynchronize(boolean restart) {
        activity.addSubscription(
                getObservableSynchronization(user.get())
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(activity.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_UPDATE, restart))
                        .doOnSubscribe(() -> {
                            mStatus.set(UPDATE_ALL);
                            error.set("");
                        })
                        .subscribe(this::updateSuccess,  this::onError)
        );
    }

    private static Observable<Boolean> getObservableSynchronization(User user) {
        return Observable
                .defer(() -> ApiModule.getRestApi().specializations())
                .map(specializations -> {
                    ApiModule
                            .getStoreIOSQLite()
                            .delete()
                            .byQuery(DELETE_ALL)
                            .prepare()
                            .executeAsBlocking();
                    ApiModule
                            .getStoreIOSQLite()
                            .put()
                            .objects(specializations)
                            .prepare()
                            .executeAsBlocking();
                    return specializations;
                })
                .flatMap(Observable::from)
                .flatMap(specialization1 -> Observable
                        .zip(
                                ApiModule
                                        .getRestApi()
                                        .question(specialization1.getId())
                                        .flatMap(Observable::from)
                                        .toList(),
                                ApiModule.getRestApi().removedQuestions(specialization1.getId()),
                                (modifiedQuestions, removedQuestions) -> {
                                    saveQuestions(modifiedQuestions);
                                    removedQuestions(removedQuestions);
                                    return true;
                                }))
                .toList()
                .map(list -> {
                    boolean result = true;
                    for(Boolean bool: list) {
                        result = result && bool;
                    }
                    return result;
                })
//                .flatMap(aBoolean -> ApiModule
//                        .getRestApi()
//                        .attempts(user.getId(), user.getLastSynchronization().getTime()))
//                .flatMap(Observable::from)
//                .map(AttemptsResponse.UnitAttempts::getAttempt)
//                .toList()
//                .flatMap(attempts -> ApiModule
//                        .getStoreIOSQLite()
//                        .put()
//                        .objects(attempts)
//                        .prepare()
//                        .asRxObservable())
//                .flatMap(attemptPutResults -> ApiModule
//                        .getRestApi().accounts(user.getId()))
//                .flatMap(accounts -> ApiModule
//                        .getStoreIOSQLite()
//                        .put()
//                        .objects(accounts)
//                        .prepare()
//                        .asRxObservable())
//                .map(accountPutResults -> true)


                ;
    }

    private static void removedQuestions(List<Long> removedQuestions) {
        StringBuilder idsBuilder = new StringBuilder();
        for (int i = 0, dataSize = removedQuestions.size(); i < dataSize; i++) {
            if (idsBuilder.length() > 0) idsBuilder.append(", ");
            idsBuilder.append(removedQuestions.get(i));
        }
        removeReferenceQuestions(idsBuilder.toString());
    }

    private static void removeReferenceQuestions(String removedIds) {
        ApiModule
                .getStoreIOSQLite()
                .delete()
                .byQuery(QuestionTable.deleteIds(removedIds))
                .prepare()
                .executeAsBlocking();

        ApiModule
                .getStoreIOSQLite()
                .delete()
                .byQuery(AttemptTable.deleteIds(removedIds))
                .prepare()
                .executeAsBlocking();

        ApiModule
                .getStoreIOSQLite()
                .delete()
                .byQuery(AnswerTable.deleteIds(removedIds))
                .prepare()
                .executeAsBlocking();
    }

    private static void saveQuestions(List<QuestionResponse.ModifiedQuestion> modifiedQuestions) {
        StringBuilder idsBuilder = new StringBuilder();
        for (int i = 0, dataSize = modifiedQuestions.size(); i < dataSize; i++) {
            QuestionResponse.ModifiedQuestion question = modifiedQuestions.get(i);
            if (idsBuilder.length() > 0) idsBuilder.append(", ");
            idsBuilder.append(question.questionId);
        }
        removeReferenceQuestions(idsBuilder.toString());

        for (int i = 0, dataSize = modifiedQuestions.size(); i < dataSize; i++) {
            QuestionResponse.ModifiedQuestion question = modifiedQuestions.get(i);

            ApiModule
                    .getStoreIOSQLite()
                    .put()
                    .object(question.getQuestion())
                    .prepare()
                    .executeAsBlocking();

            ApiModule
                    .getStoreIOSQLite()
                    .put()
                    .objects(question.answers)
                    .prepare()
                    .executeAsBlocking();
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
    @IntDef({UPDATE_ALL, NOTHING})
    private @interface Status{}
    private static final int UPDATE_ALL = 0;
    private static final int NOTHING = 2;

    private static @Status int getStatusFromInt(int from) {
        switch (from) {
            case UPDATE_ALL:
            case NOTHING:
                return from;
            default:
                return NOTHING;
        }
    }
}
