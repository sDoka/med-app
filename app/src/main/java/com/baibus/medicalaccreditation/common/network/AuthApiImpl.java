package com.baibus.medicalaccreditation.common.network;

import com.baibus.medicalaccreditation.common.network.entities.RegistrationResponse;
import com.baibus.medicalaccreditation.common.network.retrofit.AuthApi;

import rx.Observable;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 18.05.2017
 * Time: 23:53
 * To change this template use File | settings | File Templates.
 */
public class AuthApiImpl {

    private AuthApi mAuthApi;

    public AuthApiImpl(AuthApi authApi) {
        mAuthApi = authApi;
    }

    public Observable<RegistrationResponse> login(String email, String password, String deviceKey) {
        return mAuthApi
                .login(email, password, deviceKey)
                .map(apiResponseResponse -> apiResponseResponse.body().data);
    }

    public Observable<RegistrationResponse> registration(String email, String name, String password,
                                                         String female, String deviceKey) {
        return mAuthApi
                .registration(email, name, password, female, deviceKey)
                .map(apiResponseResponse -> apiResponseResponse.body().data);
    }

    public Observable<RegistrationResponse> check() {
        return mAuthApi
                .check()
                .map(apiResponseResponse -> apiResponseResponse.body().data);
    }

}
