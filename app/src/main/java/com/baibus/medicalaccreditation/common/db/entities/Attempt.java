package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 30.04.2017
 * Time: 17:13
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Attempt {

    @Nullable
    private final Long mId;

    private final long mQuestionId;

    private final long mAnswerId;

    private final long mTime;

    @ParcelConstructor
    Attempt(Long id, long questionId, long answerId, long time) {
        this.mId = id;
        this.mQuestionId = questionId;
        this.mAnswerId = answerId;
        this.mTime = time;
    }

    @NonNull
    public static Attempt newInstance(Long id, long questionId, long answerId, long time) {
        return new Attempt(id, questionId, answerId, time);
    }

    @Nullable
    public Long getId() {
        return mId;
    }

    public long getQuestionId() {
        return mQuestionId;
    }

    public long getAnswerId() {
        return mAnswerId;
    }

    public long getTime() {
        return mTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attempt attempt = (Attempt) o;

        if (mQuestionId != attempt.mQuestionId) return false;
        if (mAnswerId != attempt.mAnswerId) return false;
        if (mTime != attempt.mTime) return false;
        return mId != null ? mId.equals(attempt.mId) : attempt.mId == null;

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (int) (mQuestionId ^ (mQuestionId >>> 32));
        result = 31 * result + (int) (mAnswerId ^ (mAnswerId >>> 32));
        result = 31 * result + (int) (mTime ^ (mTime >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "id=" + mId +
                ", questionId=" + mQuestionId +
                ", answerId=" + mAnswerId +
                ", time=" + mTime +
                '}';
    }
}
