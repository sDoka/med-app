package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 30.04.2017
 * Time: 17:13
 * To change this template use File | settings | File Templates.
 */
@Parcel(Parcel.Serialization.BEAN)
public class User {

    private final long mId;

    @NonNull
    private final String mName;

    @NonNull
    private final String mEmail;

    @NonNull
    private final Date mLastSynchronization;

    private final long mDeviceId;

    @ParcelConstructor
    public User(long id, @NonNull String name, @NonNull String email,
                @NonNull Date lastSynchronization, long deviceId) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
        this.mLastSynchronization = lastSynchronization;
        this.mDeviceId = deviceId;
    }

    @NonNull
    public static User newInstance(long id, @NonNull String name, @NonNull String email,
                                   @NonNull Date lastSynchronization, long deviceId) {
        return new User(id, name, email, lastSynchronization, deviceId);
    }

    public long getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public String getEmail() {
        return mEmail;
    }

    @NonNull
    public Date getLastSynchronization() {
        return mLastSynchronization;
    }

    public long getDeviceId() {
        return mDeviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        return mId == that.mId
                && mDeviceId == that.mDeviceId
                && TextUtils.equals(mName, that.mName)
                && TextUtils.equals(mEmail, that.mEmail)
                && mLastSynchronization.equals(that.mLastSynchronization);
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + mName.hashCode();
        result = 31 * result + mEmail.hashCode();
        result = 31 * result + mLastSynchronization.hashCode();
        result = 31 * result + (int) (mDeviceId ^ (mDeviceId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + mId +
                ", name='" + mName + '\'' +
                ", email='" + mEmail + '\'' +
                ", lastSynchronization=" + mLastSynchronization +
                ", deviceId=" + mDeviceId +
                '}';
    }
}
