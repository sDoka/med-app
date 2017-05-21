package com.baibus.medicalaccreditation.common.network.retrofit;


import com.baibus.medicalaccreditation.common.network.ApiResponse;
import com.baibus.medicalaccreditation.common.network.entities.RegistrationResponse;

import retrofit2.Response;
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

    String AUTH_URL = "user-managment/";

    @FormUrlEncoded
    @POST(AUTH_URL + "get-user")
    Observable<Response<ApiResponse<RegistrationResponse>>> login(@Field("email") String email,
                                                                  @Field("password") String password,
                                                                  @Field("deviceKey") String deviceKey);

    @FormUrlEncoded
    @POST(AUTH_URL + "add-user")
    Observable<Response<ApiResponse<RegistrationResponse>>> registration(@Field("email") String email,
                                  @Field("name") String name,
                                  @Field("password") String password,
                                  @Field("male") String female,
                                  @Field("deviceKey") String deviceKey);

    @GET("brr-user")
    Observable<Response<ApiResponse<RegistrationResponse>>> check();

}
