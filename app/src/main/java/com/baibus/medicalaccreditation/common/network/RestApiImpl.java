package com.baibus.medicalaccreditation.common.network;

import com.baibus.medicalaccreditation.common.network.entities.QuestionResponse;
import com.baibus.medicalaccreditation.common.network.entities.SpecializationsResponse;
import com.baibus.medicalaccreditation.common.network.retrofit.RestApi;

import java.util.List;

import rx.Observable;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 23:03
 * To change this template use File | settings | File Templates.
 */
public class RestApiImpl {

    private RestApi mRestApi;

    public RestApiImpl(RestApi restApi) {
        mRestApi = restApi;
    }

    public Observable<List<QuestionResponse>> question() {
        return mRestApi
                .questions()
                .map(apiResponseResponse -> apiResponseResponse.body().data);
    }


    public Observable<SpecializationsResponse> specializations() {
        return mRestApi
                .specializations()
                .map(apiResponseResponse -> apiResponseResponse.body().data);
    }
}
