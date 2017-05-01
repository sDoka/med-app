package com.baibus.medicalaccreditation.common.network;

import com.baibus.medicalaccreditation.common.db.entities.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 23:03
 * To change this template use File | settings | File Templates.
 */
public interface RestApi {

    @POST("/synchronize-app")
    Observable<Question> question();

    Gson gson = new GsonBuilder()
            //TODO custom serialize
            .create();
}
