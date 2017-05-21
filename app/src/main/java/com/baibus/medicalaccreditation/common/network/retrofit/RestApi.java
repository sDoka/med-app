package com.baibus.medicalaccreditation.common.network.retrofit;

import com.baibus.medicalaccreditation.common.network.ApiResponse;
import com.baibus.medicalaccreditation.common.network.entities.QuestionResponse;
import com.baibus.medicalaccreditation.common.network.entities.SpecializationsResponse;

import java.util.List;

import retrofit2.Response;
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

    String AUTH_URL = "synchronize/";

    @POST(AUTH_URL + "questions")
    Observable<Response<ApiResponse<List<QuestionResponse>>>> questions();

    @POST(AUTH_URL + "specializations")
    Observable<Response<ApiResponse<SpecializationsResponse>>> specializations();

}
