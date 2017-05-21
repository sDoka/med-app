package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;

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

    private final long mId;

    private final long mQuestionId;

    private final long mAnswerId;

    private final long mTime;

    @ParcelConstructor
    Attempt(long id, long questionId, long answerId, long time) {
        this.mId = id;
        this.mQuestionId = questionId;
        this.mAnswerId = answerId;
        this.mTime = time;
    }

    @NonNull
    public static Attempt newInstance(long id, long questionId, long answerId, long time) {
        return new Attempt(id, questionId, answerId, time);
    }

    public long getId() {
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

        return mId == attempt.mId
                && mQuestionId == attempt.mQuestionId
                && mAnswerId == attempt.mAnswerId
                && mTime == attempt.mTime;

    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
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
