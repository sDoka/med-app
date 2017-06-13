package com.baibus.medicalaccreditation.result;

import android.databinding.ObservableArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.FragmentVM;
import com.baibus.medicalaccreditation.base.FragmentVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.entities.Attempt;
import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.tables.AnswerTable;
import com.baibus.medicalaccreditation.common.db.tables.AttemptTable;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.pushtorefresh.storio.StorIOException;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 01.05.2017
 * Time: 8:27
 * To change this template use File | settings | File Templates.
 */
public class ResultVM extends FragmentVM<ResultFragment> {
    private final static String TAG = ResultVM.class.getSimpleName();

    private final static String BUNDLE_ITEMS = "items";

    private final static int LOADER_GET_QUESTIONS = 0;

    public final ObservableArrayList<ResultQuestionVM> items = new ObservableArrayList<>();
    public final ItemBinding<ResultQuestionVM> itemBinding = ItemBinding.of(BR.itemViewModel, R.layout.item_result_question);
//    public final BindingViewPagerAdapter.PageTitles<ResultQuestionVM> pageTitles = (position, item) -> "Вопрос " + item.question.get().getIndex();
    private final OnPropertyChangedCallback mSpecializationChanged = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int property) {
            loadQuestions(true);
//            loadStatistics(true);
        }
    };

    private ResultVM(ResultFragment fragment, @NonNull Bundle savedInstanceState) {
        super(fragment, savedInstanceState);
        ArrayList<ResultQuestionVM> questionArrayList = Parcels
                .unwrap(savedInstanceState.getParcelable(BUNDLE_ITEMS));
        if (questionArrayList != null) setItems(questionArrayList);
        specialization.addOnPropertyChangedCallback(mSpecializationChanged);
        setTitle("Результаты");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (items.isEmpty()) loadQuestions(false);
//        if (items.isEmpty()) loadStatistics(false);
    }

//    public void setTitle(int position) {
//        setTitle(pageTitles.getPageTitle(position, items.get(position)));
//        this.position.set(position);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<ResultQuestionVM> it = new ArrayList<>(items.size());
        it.addAll(items);
        outState.putParcelable(BUNDLE_ITEMS, Parcels.wrap(it));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        specialization.removeOnPropertyChangedCallback(mSpecializationChanged);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restart:
                fragment.startTestAgain();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setItems(List<ResultQuestionVM> questions) {
        this.items.clear();
        this.items.addAll(questions);
    }

    private void loadStatistics(boolean restart) {
        fragment.addSubscription(
                getObservableQuestions(specialization.get())
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_GET_QUESTIONS, restart))
                        .doOnSubscribe(() -> isShowProgress.set(true))
                        .subscribe(value -> {
                            isShowProgress.set(false);
                            setItems(value);
                        }, throwable -> {
                            isShowProgress.set(false);
                            throw Exceptions.propagate(throwable);
                        })
        );
    }

    private void loadQuestions(boolean restart) {
        fragment.addSubscription(
                getObservableQuestions(specialization.get())
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .observeOn(AndroidSchedulers.from(fragment.mainHandler.getLooper()))
                        .compose(RxLoader.from(this, LOADER_GET_QUESTIONS, restart))
                        .doOnSubscribe(() -> isShowProgress.set(true))
                        .subscribe(value -> {
                            isShowProgress.set(false);
                            setItems(value);
                        }, throwable -> {
                            isShowProgress.set(false);
                            throw Exceptions.propagate(throwable);
                        })
        );
    }

    private static Observable<List<ResultQuestionVM>> getObservableQuestions(Specialization specialization) {
        return Observable
                .defer(() -> {
                    Log.d(TAG, "INNER LOADER_GET_QUESTIONS" + ActivityVM.isMainLooper());
                    try {
                        if (specialization == null) {
                            return Observable.error(new IllegalArgumentException("Specialization must not null"));
                        } else {
                            return ApiModule
                                    .getStoreIOSQLite()
                                    .get()
                                    .listOfObjects(Attempt.class)
                                    .withQuery(AttemptTable.querySpecializationId(specialization.getId()))
                                    .prepare()
                                    .asRxObservable();
                        }
                    } catch (StorIOException e) {
                        return Observable.error(e);
                    }
                })
                .map(attempts -> {
                    List<ResultQuestionVM> result = new ArrayList<>();
                    for (int i = 0, attemptsSize = attempts.size(); i < attemptsSize; i++) {
                        Attempt attempt = attempts.get(i);
                        Question question = ApiModule
                                .getStoreIOSQLite()
                                .get()
                                .object(Question.class)
                                .withQuery(QuestionTable.queryId(attempt.getQuestionId()))
                                .prepare()
                                .executeAsBlocking();
                        ArrayList<Answer> answers = new ArrayList<>();
                        if (question != null) {
                            answers.addAll(ApiModule
                                    .getStoreIOSQLite()
                                    .get()
                                    .listOfObjects(Answer.class)
                                    .withQuery(AnswerTable.queryQuestionId(question.getId()))
                                    .prepare()
                                    .executeAsBlocking());
                            result.add(new ResultQuestionVM(question, answers, attempt));
                        }
                    }
                    return result;
                });
    }

    static class FactoryVM implements FragmentVMFactory<ResultVM, ResultFragment> {
        @NonNull
        @Override
        public ResultVM create(ResultFragment view) {
            return new ResultVM(view, view.getArguments());
        }

        @NonNull
        @Override
        public ResultVM restore(ResultFragment view, @NonNull Bundle savedInstanceState) {
            return new ResultVM(view, savedInstanceState);
        }
    }
}
