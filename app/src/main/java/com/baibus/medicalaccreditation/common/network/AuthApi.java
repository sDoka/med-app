package com.baibus.medicalaccreditation.common.network;

import com.baibus.medicalaccreditation.common.db.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 23:07
 * To change this template use File | settings | File Templates.
 */
public interface AuthApi {

    @FormUrlEncoded
    @POST("/authorize-user")
    Observable<User> login(@Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("/add-user")
    Observable<User> registration(@Field("email") String email,
                                  @Field("name") String name,
                                  @Field("password") String password);

    @FormUrlEncoded
    @GET("/get-user")
    Observable<User> check();

    Gson gson = new GsonBuilder()
            //TODO custom serialize
            .create();
}
