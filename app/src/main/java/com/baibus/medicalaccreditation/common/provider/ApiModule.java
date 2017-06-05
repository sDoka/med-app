package com.baibus.medicalaccreditation.common.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.common.network.AuthApiImpl;
import com.baibus.medicalaccreditation.common.network.RestApiImpl;
import com.baibus.medicalaccreditation.common.network.retrofit.AuthApi;
import com.baibus.medicalaccreditation.common.network.retrofit.RestApi;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private static Map<String, Object> INSTANCES = new HashMap<>();
    private static final Object SYNC = new Object();

    public final static String EXTRA_PROGRESS_READ = "EXTRA_PROGRESS_READ";
    public final static String EXTRA_PROGRESS_LENGTH = "EXTRA_PROGRESS_LENGTH";

    @SuppressLint("HardwareIds")
    public static String getDeviceKey() {
        return ((TelephonyManager) MedApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    public static RestApiImpl getRestApi() {
        RestApiImpl api = findApiInstance(RestApiImpl.class);
        if (api == null) {
            api = new RestApiImpl(RetrofitProvider
                    .provide(OkHttpProvider.getOkHttpClient())
                    .create(RestApi.class)
            );
            addApiInstance(RestApiImpl.class, api);
        }
        return api;
    }

    public static RestApiImpl getRestApiMock(String body) {
        return new RestApiImpl(RetrofitProvider
                .provide(OkHttpProvider.getOkHttpClient(), body)
                .create(RestApi.class)
        );
    }

    public static void shutdownServer() {
        try {
            if (RetrofitProvider.server == null) {
                return;
            }
            RetrofitProvider.server.shutdown();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @NonNull
    public static AuthApiImpl getAuthApi() {
        AuthApiImpl api = findApiInstance(AuthApiImpl.class);
        if (api == null) {
            api = new AuthApiImpl(RetrofitProvider
                    .provide(OkHttpProvider.getOkHttpClient())
                    .create(AuthApi.class)
            );
            addApiInstance(AuthApiImpl.class, api);
        }
        return api;
    }

    public static StorIOSQLite getStoreIOSQLite() {
        return StorIOProvider.getStorIO(MedApplication.getInstance());
    }

    private static <T> void addApiInstance(@NonNull Class<T> clazz, T client) {
        synchronized (SYNC) {
            INSTANCES.put(clazz.getCanonicalName(), client);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T> T findApiInstance(@NonNull Class<T> clazz) {
        synchronized (SYNC) {
            Object object = INSTANCES.get(clazz.getCanonicalName());
            if (clazz.isInstance(object)) {
                return (T) object;
            }
            return null;
        }
    }
}