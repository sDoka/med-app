package com.baibus.medicalaccreditation.testing;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.base.ActivityVM;
import com.baibus.medicalaccreditation.base.FragmentVM;
import com.baibus.medicalaccreditation.base.FragmentVMFactory;
import com.baibus.medicalaccreditation.base.RxLoader;
import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.db.tables.AnswerTable;
import com.baibus.medicalaccreditation.common.db.tables.QuestionTable;
import com.baibus.medicalaccreditation.common.provider.ApiModule;
import com.pushtorefresh.storio.StorIOException;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
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
public class TestingVM extends FragmentVM<TestingFragment> implements OnQuestionAttemptedListener {
    private final static String TAG = TestingVM.class.getSimpleName();

    private final static String BUNDLE_ITEMS = "items";
    private final static String BUNDLE_POSITION = "position";

    private final static int LOADER_GET_QUESTIONS = 0;

    public final ObservableInt position = new ObservableInt();
    public final ObservableArrayList<QuestionVM> items = new ObservableArrayList<>();
    public final ItemBinding<QuestionVM> itemBinding = ItemBinding.of(BR.itemViewModel, R.layout.item_question);
    public final BindingViewPagerAdapter.PageTitles<QuestionVM> pageTitles = (position, item) -> "Item " + (position + 1);
    private final OnPropertyChangedCallback mSpecializationChanged = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int property) {
            loadQuestions(true);
        }
    };

    private TestingVM(TestingFragment fragment, @NonNull Bundle savedInstanceState) {
        super(fragment, savedInstanceState);
        ArrayList<QuestionVM> questionArrayList = Parcels
                .unwrap(savedInstanceState.getParcelable(BUNDLE_ITEMS));
        if (questionArrayList != null) setItems(questionArrayList);
        position.set(savedInstanceState.getInt(BUNDLE_POSITION));
        if (items.isEmpty()) loadQuestions(false);
        specialization.addOnPropertyChangedCallback(mSpecializationChanged);
    }

    public void setTitle(int position) {
        setTitle(pageTitles.getPageTitle(position, items.get(position)));
        this.position.set(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_ITEMS, Parcels.wrap(items));
        outState.putInt(BUNDLE_POSITION, position.get());
    }

    @Override
    public void onQuestionAttempted(Question question, int position) {
        if (position == items.size() - 1) {
            fragment.showResult();
        } else {
            fragment.setCurrentItem(position + 1, true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        specialization.removeOnPropertyChangedCallback(mSpecializationChanged);
    }

    private void setItems(List<QuestionVM> questions) {
        for (QuestionVM question: questions) {
            question.setListener(this);
        }
        this.items.clear();
        this.items.addAll(questions);
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

    private static Observable<List<QuestionVM>> getObservableQuestions(Specialization specialization) {
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
                                    .listOfObjects(Question.class)
                                    .withQuery(QuestionTable.querySpecializationId(specialization.getId()))
                                    .prepare()
                                    .asRxObservable();
                        }
                    } catch (StorIOException e) {
                        return Observable.error(e);
                    }
                })
                .map(questions -> {
                    List<QuestionVM> result = new ArrayList<>();
                    for (int i = 0, questionsSize = questions.size(); i < questionsSize; i++) {
                        Question question = questions.get(i);
                        List<Answer> answers = ApiModule
                                .getStoreIOSQLite()
                                .get()
                                .listOfObjects(Answer.class)
                                .withQuery(AnswerTable.queryQuestionId(specialization.getId()))
                                .prepare()
                                .executeAsBlocking();
                        Collections.shuffle(answers);
                        result.add(new QuestionVM(i, question, answers));
                    }
                    return result;
                });
    }

    static class FactoryVM implements FragmentVMFactory<TestingVM, TestingFragment> {
        @NonNull
        @Override
        public TestingVM create(TestingFragment view) {
            return new TestingVM(view, view.getArguments());
        }

        @NonNull
        @Override
        public TestingVM restore(TestingFragment view, @NonNull Bundle savedInstanceState) {
            return new TestingVM(view, savedInstanceState);
        }
    }
}
