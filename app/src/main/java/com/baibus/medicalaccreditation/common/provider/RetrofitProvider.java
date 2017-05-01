package com.baibus.medicalaccreditation.common.provider;

import android.support.annotation.NonNull;

import com.baibus.medicalaccreditation.common.network.RxErrorHandlingCallAdapterFactory;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 22:47
 * To change this template use File | settings | File Templates.
 */
class RetrofitProvider {

    private static volatile RetrofitProvider instance;

    private Retrofit mRetrofit;

    private Map<String, Object> mRestApiInstances = new HashMap<>();


    static RetrofitProvider getInstance(Gson gson, OkHttpClient client) {
        if (instance == null) {
            synchronized (StorIOProvider.class) {
                if (instance == null) {
                    instance = new RetrofitProvider(gson, client);
                }
            }
        }

        return instance;
    }

    private RetrofitProvider(Gson gson, OkHttpClient client) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(Schedulers.io()))
                .client(client)
                .build();
    }


    /** thread safe, ensure every Api will be created only once **/
    @SuppressWarnings("unchecked")
    synchronized <T> T getRestApi(@NonNull Class<T> clazz) {

        Object object = mRestApiInstances.get(clazz.getCanonicalName());
        if (clazz.isInstance(object)) {
            return (T) object;
        }
        T client = mRetrofit.create(clazz);
        mRestApiInstances.put(clazz.getCanonicalName(), client);
        return client;
    }
}
