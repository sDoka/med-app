package com.baibus.medicalaccreditation.common.provider;

import com.baibus.medicalaccreditation.common.network.DateDeserializer;
import com.baibus.medicalaccreditation.common.network.DateSerializer;
import com.baibus.medicalaccreditation.common.network.RxErrorHandlingCallAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
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

    private RetrofitProvider() {
        throw new AssertionError();
    }

    private static volatile Retrofit instance;

    static volatile MockWebServer server;
    static Retrofit provide(OkHttpClient client) {
        if (instance == null) {
            synchronized (StorIOProvider.class) {
                if (instance == null) {

                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Date.class, new DateDeserializer())
                            .create();
                    instance = new Retrofit.Builder()
                            .baseUrl("http://med.dokstudio.ru/o/med-app-rest/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(Schedulers.io()))
                            .client(client)
                            .build();
                }
            }
        }
        return instance;
    }

    static Retrofit provide(OkHttpClient client, String body) {
        try {
            server = new MockWebServer();
            server.start();
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(body));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        return new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(Schedulers.io()))
                .client(client)
                .build();
    }
}
