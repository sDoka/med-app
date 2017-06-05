package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 14.05.2017
 * Time: 23:25
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Account {

    @SerializedName("userId")
    private final long mUserId;

    @SerializedName("type")
    private final long mType;

    @SerializedName("activationDate")
    private final long mActivationDate;

    @SerializedName("expirationDate")
    private final long mExpirationDate;

    @ParcelConstructor
    Account(@ParcelProperty("userId") long userId, @ParcelProperty("type") long type,
            @ParcelProperty("activationDate") long activationDate,
            @ParcelProperty("expirationDate") long expirationDate) {
        this.mUserId = userId;
        this.mType = type;
        this.mActivationDate = activationDate;
        this.mExpirationDate = expirationDate;
    }

    @NonNull
    public static Account newInstance(long userId, long type, long activationDate, long expirationDate) {
        return new Account(userId, type, activationDate, expirationDate);
    }

    @ParcelProperty("userId")
    public long getUserId() {
        return mUserId;
    }

    @ParcelProperty("type")
    public long getType() {
        return mType;
    }

    @ParcelProperty("activationDate")
    public long getActivationDate() {
        return mActivationDate;
    }

    @ParcelProperty("expirationDate")
    public long getExpirationDate() {
        return mExpirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (mUserId != account.mUserId) return false;
        if (mType != account.mType) return false;
        if (mActivationDate != account.mActivationDate) return false;
        return mExpirationDate == account.mExpirationDate;

    }

    @Override
    public int hashCode() {
        int result = (int) (mUserId ^ (mUserId >>> 32));
        result = 31 * result + (int) (mType ^ (mType >>> 32));
        result = 31 * result + (int) (mActivationDate ^ (mActivationDate >>> 32));
        result = 31 * result + (int) (mExpirationDate ^ (mExpirationDate >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + mUserId +
                ", type=" + mType +
                ", activationDate=" + mActivationDate +
                ", expirationDate=" + mExpirationDate +
                '}';
    }
}
