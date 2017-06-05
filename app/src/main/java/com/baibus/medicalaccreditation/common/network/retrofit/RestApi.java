package com.baibus.medicalaccreditation.common.network.retrofit;

import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.network.ApiResponse;
import com.baibus.medicalaccreditation.common.network.entities.AccountResponse;
import com.baibus.medicalaccreditation.common.network.entities.AttemptsResponse;
import com.baibus.medicalaccreditation.common.network.entities.QuestionResponse;
import com.baibus.medicalaccreditation.common.network.entities.RemovedQuestionResponse;
import com.baibus.medicalaccreditation.common.network.entities.SpecializationsResponse;

import java.util.Date;
import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 22.04.2017
 * Time: 23:03
 * To change this template use File | settings | File Templates.
 */
public interface RestApi {

    String AUTH_URL = "synchronization/";

    @GET(AUTH_URL + "get-specializations")
    Observable<Response<ApiResponse<SpecializationsResponse>>> specializations();

    @GET(AUTH_URL + "get-all-test-units")
    Observable<Response<ApiResponse<QuestionResponse>>> questions(@Query("specializationId") long id);

    @GET(AUTH_URL + "get-removed-test-units")
    Observable<Response<ApiResponse<RemovedQuestionResponse>>> removedQuestions(@Query("specializationId") long id);

    @GET(AUTH_URL + "get-user-attempts")
    Observable<Response<ApiResponse<AttemptsResponse>>> attempts(@Query("userId") long id, @Query("lastSynchronizationDate") long date);

    @GET(AUTH_URL + "get-user-account-types")
    Observable<Response<ApiResponse<AccountResponse>>> accounts(@Query("userId") long id);

}
