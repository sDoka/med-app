package com.baibus.medicalaccreditation.common.provider;

import android.content.Context;

import com.baibus.medicalaccreditation.common.network.AuthApi;
import com.baibus.medicalaccreditation.common.network.RestApi;
import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import okhttp3.OkHttpClient;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:47
 * To change this template use File | settings | File Templates.
 */
public class ApiModule {
    private ApiModule() {
        throw new AssertionError();
    }

    public final static String EXTRA_PROGRESS_READ = "EXTRA_PROGRESS_READ";
    public final static String EXTRA_PROGRESS_LENGTH = "EXTRA_PROGRESS_LENGTH";

    public final static int HTTP_CODE_BAD_REQUEST = 400;
    public final static int HTTP_CODE_UNAUTHORIZED = 401;
    public final static int HTTP_CODE_PAYMENT_REQUIRED = 402;
    public final static int HTTP_CODE_FORBIDDEN = 403;
    public final static int HTTP_CODE_NOT_FOUND = 404;

    public static boolean isClientError(int code) {
        return isBetween(code, 400, 499);
    }
    public static boolean isServerError(int code) {
        return isBetween(code, 500, 599);
    }

    public static RestApi getRestApi() {
        OkHttpClient client = OkHttpProvider.getOkHttpClient();
        return RetrofitProvider.getInstance(RestApi.gson, client).getRestApi(RestApi.class);
    }

    public static AuthApi getAuthApi() {
        OkHttpClient client = OkHttpProvider.getOkHttpClient();
        return RetrofitProvider.getInstance(AuthApi.gson, client).getRestApi(AuthApi.class);
    }

    public static StorIOSQLite getStoreIOSQLite(Context context) {
        return StorIOProvider.getStorIO(context);
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}