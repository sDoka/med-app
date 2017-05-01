package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 21.04.2017
 * Time: 16:11
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Question {

    private final long mId;

    private final long mIndex;

    private final long mSpecializationId;

    @NonNull
    private final String mText;

    @Nullable
    private final String mFilePath;

    @ParcelConstructor
    Question(long id, long index, long specializationId, @NonNull String text,
                     @Nullable String filePath) {
        this.mId = id;
        this.mIndex = index;
        this.mSpecializationId = specializationId;
        this.mText = text;
        this.mFilePath = filePath;
    }

    @NonNull
    public static Question newQuestion(long id, long index, long specializationId,
                                       @NonNull String text, @Nullable String filePath) {
        return new Question(id, index, specializationId, text, filePath);
    }

    public long getId() {
        return mId;
    }

    public long getIndex() {
        return mIndex;
    }

    public long getSpecializationId() {
        return mSpecializationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question that = (Question) o;

        return mId == that.mId
                && mIndex == that.mIndex
                && mSpecializationId == that.mSpecializationId
                && TextUtils.equals(mText, that.mText)
                && TextUtils.equals(mFilePath, that.mFilePath);
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (int) (mIndex ^ (mIndex >>> 32));
        result = 31 * result + (int) (mSpecializationId ^ (mSpecializationId >>> 32));
        result = 31 * result + mText.hashCode();
        result = 31 * result + (mFilePath != null ? mFilePath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + mId +
                ", index=" + mIndex +
                ", specializationId=" + mSpecializationId +
                ", text='" + mText + '\'' +
                ", filePath='" + mFilePath + '\'' +
                '}';
    }
}