package com.baibus.medicalaccreditation.common.db.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

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
public class Specialization {

    @SerializedName("specializationId")
    private final long mId;

    @SerializedName("nameLocalizationKey")
    @NonNull
    private final String mName;

    @SerializedName("specializationIcon")
    @NonNull
    private final String mFilePath;

    @ParcelConstructor
    Specialization(long id, @NonNull String name, @NonNull String filePath) {
        this.mId = id;
        this.mName = name;
        this.mFilePath = filePath;
    }

    @NonNull
    public static Specialization newInstance(long id, @NonNull String name, @NonNull String filePath) {
        return new Specialization(id, name, filePath);
    }

    public long getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public String getFilePath() {
        return mFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Specialization that = (Specialization) o;

        return mId == that.mId
                && TextUtils.equals(mName, that.mName)
                && TextUtils.equals(mFilePath, that.mFilePath);
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + mName.hashCode();
        result = 31 * result + mFilePath.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Specialization{" +
                "id=" + mId +
                ", name='" + mName + '\'' +
                ", filePath='" + mFilePath + '\'' +
                '}';
    }
}
