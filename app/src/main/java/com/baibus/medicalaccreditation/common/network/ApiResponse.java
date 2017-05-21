package com.baibus.medicalaccreditation.common.network;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 18.05.2017
 * Time: 19:49
 * To change this template use File | settings | File Templates.
 */
public class ApiResponse<T> {

    @SerializedName("responseCode")
    public int code;

    @SerializedName("responseStatus")
    public String status;

    @SerializedName("responseBody")
    public T data;

    @SerializedName("responseError")
    public String error;
}
