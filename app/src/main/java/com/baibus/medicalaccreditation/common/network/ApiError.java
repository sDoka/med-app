package com.baibus.medicalaccreditation.common.network;

import java.io.IOException;

/**
 * Created by Android Studio.
 * User: yanbaev.is
 * Date: 18.05.2017
 * Time: 21:41
 * To change this template use File | settings | File Templates.
 */
public class ApiError extends RuntimeException {

    public static ApiError unauthenticatedError(String url, String status) {
        return new ApiError(401, status, url, Kind.UNAUTHENTICATED);
    }

    public static ApiError clientError(String url, int code, String status) {
        return new ApiError(code, status, url, Kind.CLIENT);
    }

    public static ApiError serverError(String url, int code, String status) {
        return new ApiError(code, status, url, Kind.SERVER);
    }

    public static ApiError networkError(IOException exception) {
        return new ApiError(exception.getMessage(), Kind.NETWORK, exception);
    }

    public static ApiError unexpectedError(Throwable exception) {
        return new ApiError(exception.getMessage(), Kind.UNEXPECTED, exception);
    }

    /** Identifies the event kind which triggered a {@link ApiError}. */
    public enum Kind {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** A 401 HTTP status code was received from the server.*/
        UNAUTHENTICATED,
        /** A [400..500) (expected 401) HTTP status code was received from the server. */
        CLIENT,
        /** A [500..600) HTTP status code was received from the server. */
        SERVER,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    private final int code;
    private final String url;
    private final Kind kind;

    public int getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public Kind getKind() {
        return kind;
    }

    private ApiError(String message, Kind kind, Throwable exception) {
        super(message, exception);
        this.code = 0;
        this.url = null;
        this.kind = kind;
    }

    private ApiError(int code, String message, String url, Kind kind) {
        super(message);
        this.code = code;
        this.url = url;
        this.kind = kind;
    }


}
