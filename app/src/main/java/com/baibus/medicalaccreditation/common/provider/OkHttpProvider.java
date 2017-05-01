package com.baibus.medicalaccreditation.common.provider;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import com.baibus.medicalaccreditation.MedApplication;
import com.baibus.medicalaccreditation.common.network.ProgressResponseBody;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import static com.baibus.medicalaccreditation.common.provider.ApiModule.EXTRA_PROGRESS_LENGTH;
import static com.baibus.medicalaccreditation.common.provider.ApiModule.EXTRA_PROGRESS_READ;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:47
 * To change this template use File | settings | File Templates.
 */
class OkHttpProvider {
    private OkHttpProvider() {
        throw new AssertionError();
    }

    private final static long CONNECT_TIMEOUT_SECOND = 60;
    private final static long READ_TIMEOUT_SECOND = 60;
    private final static long WRITE_TIMEOUT_SECOND = 60;


    private static volatile OkHttpClient instance;

    static OkHttpClient getOkHttpClient() {
        if (instance == null) {
            synchronized (OkHttpProvider.class) {
                if (instance == null) {
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(BODY);
                    instance = new OkHttpClient.Builder()
                            .addInterceptor(httpLoggingInterceptor)
                            .addNetworkInterceptor(createProgressInterceptor())
                            .addNetworkInterceptor(new StethoInterceptor())
                            .connectTimeout(CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT_SECOND, TimeUnit.SECONDS)
                            .build();
                }
            }
        }

        return instance;
    }

    private static Interceptor createProgressInterceptor() {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse
                    .newBuilder()
                    .body(
                            new ProgressResponseBody(
                                    originalResponse.body(),
                                    (read, length) -> LocalBroadcastManager
                                            .getInstance(MedApplication.getInstance())
                                            .sendBroadcast(
                                                    new Intent(originalResponse
                                                            .request()
                                                            .url()
                                                            .toString()
                                                    )
                                                            .putExtra(EXTRA_PROGRESS_READ, read)
                                                            .putExtra(EXTRA_PROGRESS_LENGTH, length)
                                            )
                            )
                    )
                    .build();
        };
    }
}
