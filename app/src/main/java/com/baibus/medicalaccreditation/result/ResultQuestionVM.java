package com.baibus.medicalaccreditation.result;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.baibus.medicalaccreditation.BR;
import com.baibus.medicalaccreditation.R;
import com.baibus.medicalaccreditation.common.db.entities.Answer;
import com.baibus.medicalaccreditation.common.db.entities.Attempt;
import com.baibus.medicalaccreditation.common.db.entities.Question;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 12.06.2017
 * Time: 20:31
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class ResultQuestionVM extends BaseObservable {

    public final ObservableField<Question> question = new ObservableField<>();
    public final ObservableArrayList<Answer> answers = new ObservableArrayList<>();
    public final ObservableField<Attempt> attempt = new ObservableField<>();

    @Transient
    public final OnItemBind<Answer> itemBinding =
            (itemBinding, position, item) -> itemBinding.set(BR.itemViewModel, R.layout.item_result_answer)
                    .bindExtra(BR.resultAnswerId, attempt.get() == null ? -1L: attempt.get().getAnswerId())
                    .bindExtra(BR.index, position + 1);

    @ParcelConstructor
    ResultQuestionVM(@ParcelProperty("question") Question question,
                     @ParcelProperty("answers") List<Answer> answers,
                     @ParcelProperty("attempt") Attempt attempt) {
        this.attempt.set(attempt);
        this.question.set(question);
        if (answers != null) {
            this.answers.addAll(answers);
        }
    }


    @ParcelProperty("attempt")
    Attempt getParcelAttempt() {
        return attempt.get();
    }
    @ParcelProperty("question")
    Question getParcelQuestion() {
        return question.get();
    }
    @ParcelProperty("answers")
    List<Answer> getParcelAnswers() {
        return new ArrayList<>(answers);
    }
}
