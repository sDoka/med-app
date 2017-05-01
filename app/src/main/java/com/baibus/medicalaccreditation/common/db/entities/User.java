package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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
public class User {

    private final long mId;

    @NonNull
    private final String mName;

    @NonNull
    private final String mEmail;

    @ParcelConstructor
    public User(long id, @NonNull String name, @NonNull String email) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
    }

    @NonNull
    public static User newUser(long id, @NonNull String name, @NonNull String email) {
        return new User(id, name, email);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        return mId == that.mId
                && TextUtils.equals(mName, that.mName)
                && TextUtils.equals(mEmail, that.mEmail);
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + mName.hashCode();
        result = 31 * result + mEmail.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + mId +
                ", name='" + mName + '\'' +
                ", email='" + mEmail + '\'' +
                '}';
    }
}
