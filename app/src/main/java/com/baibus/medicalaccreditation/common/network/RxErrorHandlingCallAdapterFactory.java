package com.baibus.medicalaccreditation.common.network;

import android.text.TextUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 04.04.2017
 * Time: 17:25
 * To change this template use File | settings | File Templates.
 */
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJavaCallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJavaCallAdapterFactory.create();
    }
    private RxErrorHandlingCallAdapterFactory(Scheduler scheduler) {
        original = RxJavaCallAdapterFactory.createWithScheduler(scheduler);
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }
    public static CallAdapter.Factory create(Scheduler scheduler) {
        return new RxErrorHandlingCallAdapterFactory(scheduler);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
        @SuppressWarnings("unused")
        private final Retrofit retrofit;
        private final CallAdapter<?> wrapped;

        RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public <R> Observable<Response<ApiResponse<?>>> adapt(Call<R> call) {
            return ((Observable<Response<ApiResponse<?>>>) wrapped.adapt(call))
                    .map(response -> {
                        if (response.isSuccessful()) {
                            int code = response.body().code;
                            if (code >= 200 && code < 300 && TextUtils.equals("success", response.body().status)) {
                                return response;
                            }
                            if (code == 401) {
                                throw ApiError.unauthenticatedError(
                                        response.raw().request().url().toString(),
                                        response.body().error);
                            }
                            if (code >= 400 && code < 500) {
                                throw ApiError.clientError(
                                        response.raw().request().url().toString(),
                                        code,
                                        response.body().error);
                            }
                            if (code >= 500 && code < 600) {
                                throw ApiError.serverError(
                                        response.raw().request().url().toString(),
                                        code,
                                        response.body().error);
                            }
                            throw ApiError.unexpectedError(new RuntimeException("Unexpected response code = " + code + "; status = " + response.body().error));
                        }
                        int code = response.code();
                        if (code == 401) {
                            throw ApiError.unauthenticatedError(
                                    response.raw().request().url().toString(),
                                    "Unauthenticated");
                        }
                        if (code >= 400 && code < 500) {
                            throw ApiError.clientError(
                                    response.raw().request().url().toString(),
                                    code,
                                    "Client Error (" + code + ")");
                        }
                        if (code >= 500 && code < 600) {
                            throw ApiError.serverError(
                                    response.raw().request().url().toString(),
                                    code,
                                    "Server Error (" + code + ")");
                        }
                        throw ApiError.unexpectedError(new RuntimeException("Unexpected response code = " + code));
                    })
                    .onErrorResumeNext(throwable -> {
                        throw asApiError(throwable);
                    });
        }

        private ApiError asApiError(Throwable throwable) {

            if (throwable instanceof ApiError)
                return (ApiError) throwable;

            // A network error happened
            if (throwable instanceof IOException) {
                return ApiError.networkError((IOException) throwable);
            }
//            // We had non-200 http error
//            if (throwable instanceof HttpException) {
//                HttpException httpException = (HttpException) throwable;
//                Response response = httpException.response();
//                return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit);
//            }

            // We don't know what happened. We need to simply convert to an unknown error

            return ApiError.unexpectedError(throwable);
        }
    }
}