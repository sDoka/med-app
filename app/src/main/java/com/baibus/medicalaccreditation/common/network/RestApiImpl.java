package com.baibus.medicalaccreditation.common.network;

import com.baibus.medicalaccreditation.common.db.entities.Account;
import com.baibus.medicalaccreditation.common.db.entities.Specialization;
import com.baibus.medicalaccreditation.common.network.entities.AccountResponse;
import com.baibus.medicalaccreditation.common.network.entities.AttemptsResponse;
import com.baibus.medicalaccreditation.common.network.entities.QuestionResponse;
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

    public Observable<List<Specialization>> specializations() {
        return mRestApi
                .specializations()
                .map(apiResponseResponse -> apiResponseResponse.body().data.data);
    }

    public Observable<List<QuestionResponse.ModifiedQuestion>> question(long specializationId) {
        return mRestApi
                .questions(specializationId)
                .map(apiResponseResponse -> apiResponseResponse.body().data.data);
    }

    public Observable<List<Long>> removedQuestions(long specializationId) {
        return mRestApi
                .removedQuestions(specializationId)
                .map(apiResponseResponse -> apiResponseResponse.body().data.data.ids);
    }

    public Observable<List<AttemptsResponse.UnitAttempts>> attempts(long userId, long date) {
        return mRestApi
                .attempts(userId, date)
                .map(apiResponseResponse -> apiResponseResponse.body().data.data);
    }

    public Observable<List<Account>> accounts(long userId) {
        return mRestApi
                .accounts(userId)
                .map(apiResponseResponse -> apiResponseResponse.body().data.data);
    }

}
