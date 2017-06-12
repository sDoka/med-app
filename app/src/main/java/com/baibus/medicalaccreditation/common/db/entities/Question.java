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
    private final String mCode;

    @NonNull
    private final String mText;

    @Nullable
    private final String mFilePath;

    private final int mType;

    private final long mCreateDate;

    private final long mModifiedDate;

    @ParcelConstructor
    Question(long id, long index, long specializationId, @NonNull String code, @NonNull String text,
                     @Nullable String filePath, int type, long createDate, long modifiedDate) {
        this.mId = id;
        this.mIndex = index;
        this.mSpecializationId = specializationId;
        this.mCode = code;
        this.mText = text;
        this.mFilePath = filePath;
        this.mType = type;
        this.mCreateDate = createDate;
        this.mModifiedDate = modifiedDate;
    }

    @NonNull
    public static Question newInstance(long id, long index, long specializationId,
                                       @NonNull String code, @NonNull String text,
                                       @Nullable String filePath, int type,
                                       long createDate, long modifiedDate) {
        return new Question(id, index, specializationId, code, text, filePath, type, createDate, modifiedDate);
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
        return !TextUtils.isEmpty(mFilePath) && !TextUtils.equals("0", mFilePath);//wtf???
    }

    @NonNull
    public String getCode() {
        return mCode;
    }

    public int getType() {
        return mType;
    }

    public long getCreateDate() {
        return mCreateDate;
    }

    public long getModifiedDate() {
        return mModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return mId == question.mId
                && mIndex == question.mIndex
                && mSpecializationId == question.mSpecializationId
                && mType == question.mType
                && mCreateDate == question.mCreateDate
                && mModifiedDate == question.mModifiedDate
                && TextUtils.equals(mCode, question.mCode)
                && TextUtils.equals(mText, question.mText)
                && TextUtils.equals(mFilePath, question.mFilePath);

    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (int) (mIndex ^ (mIndex >>> 32));
        result = 31 * result + (int) (mSpecializationId ^ (mSpecializationId >>> 32));
        result = 31 * result + mCode.hashCode();
        result = 31 * result + mText.hashCode();
        result = 31 * result + (mFilePath != null ? mFilePath.hashCode() : 0);
        result = 31 * result + mType;
        result = 31 * result + (int) (mCreateDate ^ (mCreateDate >>> 32));
        result = 31 * result + (int) (mModifiedDate ^ (mModifiedDate >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + mId +
                ", index=" + mIndex +
                ", specializationId=" + mSpecializationId +
                ", code='" + mCode + '\'' +
                ", text='" + mText + '\'' +
                ", filePath='" + mFilePath + '\'' +
                ", type=" + mType +
                ", createDate=" + mCreateDate +
                ", modifiedDate=" + mModifiedDate +
                '}';
    }
}