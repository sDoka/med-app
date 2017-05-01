package com.baibus.medicalaccreditation.testing;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.entities.Question;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;
import org.parceler.Transient;

import java.lang.ref.WeakReference;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 01.05.2017
 * Time: 8:52
 * To change this template use File | settings | File Templates.
 */
@Parcel
public class QuestionVM extends BaseObservable implements OnAnswerAttemptedListener {
    @Transient
    private WeakReference<OnQuestionAttemptedListener> mListener = new WeakReference<>(null);
    public final ObservableInt position = new ObservableInt();
    public final ObservableField<Question> question = new ObservableField<>();
    @ParcelProperty("answers")
    public final ObservableArrayList<Answer> answers = new ObservableArrayList<>();
    @Transient
    public final OnItemBind<Answer> itemBinding =
            (itemBinding, position, item) -> itemBinding
                    .set(BR.itemViewModel, R.layout.item_answer)
                    .bindExtra(BR.listener, (OnAnswerAttemptedListener) this)
                    .bindExtra(BR.position, position);
    @Transient
    public final BindingListViewAdapter.ItemIds<Answer> itemIds = (position1, item) -> item.getId();

    @ParcelConstructor
    QuestionVM(@ParcelProperty("position") int position,
               @ParcelProperty("question") Question question,
               @ParcelProperty("answers") List<Answer> answers) {
        this.position.set(position);
        this.question.set(question);
        if (answers != null) {
            this.answers.addAll(answers);
        }
    }

    void setListener(OnQuestionAttemptedListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    public void onAnswerAttempted(Answer answer, int position) {
        OnQuestionAttemptedListener listener = mListener.get();
        if (listener != null) {
            listener.onQuestionAttempted(this.question.get(), this.position.get());
        }
    }

    @ParcelProperty("position")
    int getParcelPosition() {
        return position.get();
    }
    @ParcelProperty("question")
    Question getParcelQuestion() {
        return question.get();
    }
}
