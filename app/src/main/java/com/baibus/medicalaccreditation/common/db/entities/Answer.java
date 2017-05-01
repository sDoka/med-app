package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 23.04.2017
 * Time: 22:26
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Answer {

    private final long mId;

    private final long mQuestionId;

    @NonNull
    private final String mText;

    @Nullable
    private final String mFilePath;

    private final boolean mIsCorrect;

    @ParcelConstructor
    private Answer(long id, long questionId, @NonNull String text,
                   @Nullable String filePath, boolean isCorrect) {
        this.mId = id;
        this.mQuestionId = questionId;
        this.mText = text;
        this.mFilePath = filePath;
        this.mIsCorrect = isCorrect;
    }

    @NonNull
    public static Answer newAnswer(long id, long questionId, @NonNull String text,
                                   @Nullable String filePath, boolean isCorrect) {
        return new Answer(id, questionId, text, filePath, isCorrect);
    }

    public long getId() {
        return mId;
    }

    public long getQuestionId() {
        return mQuestionId;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    @Nullable
    public String getFilePath() {
        return mFilePath;
    }

    public boolean hasFilePath() {
        return !TextUtils.isEmpty(mFilePath);
    }

    public boolean isCorrect() {
        return mIsCorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer that = (Answer) o;

        return mId == that.mId
                && mQuestionId == that.mQuestionId
                && mIsCorrect == that.mIsCorrect
                && TextUtils.equals(mText, that.mText)
                && TextUtils.equals(mFilePath, that.mFilePath);
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (int) (mQuestionId ^ (mQuestionId >>> 32));
        result = 31 * result + mText.hashCode();
        result = 31 * result + (mFilePath != null ? mFilePath.hashCode() : 0);
        result = 31 * result + (mIsCorrect ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + mId +
                ", questionId=" + mQuestionId +
                ", text='" + mText + '\'' +
                ", filePath='" + mFilePath + '\'' +
                ", isCorrect=" + mIsCorrect +
                '}';
    }
}
