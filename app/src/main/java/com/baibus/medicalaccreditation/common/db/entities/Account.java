package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 14.05.2017
 * Time: 23:25
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Account {

    private final long mUserId;

    private final long mType;

    @ParcelConstructor
    Account(long userId, long type) {
        this.mUserId = userId;
        this.mType = type;
    }

    @NonNull
    public static Account newInstance(long userId, long type) {
        return new Account(userId, type);
    }

    public long getUserId() {
        return mUserId;
    }

    public long getType() {
        return mType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (mUserId != account.mUserId) return false;
        return mType == account.mType;

    }

    @Override
    public int hashCode() {
        int result = (int) (mUserId ^ (mUserId >>> 32));
        result = 31 * result + (int) (mType ^ (mType >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + mUserId +
                ", type=" + mType +
                '}';
    }
}
