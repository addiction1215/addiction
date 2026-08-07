package com.addiction.global;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String message;
    private final T data;
    private final String errorCode;


    public ApiResponse(HttpStatus httpStatus, String message, T data) {
        this(httpStatus, message, data, null);
    }

    public ApiResponse(HttpStatus httpStatus, String message, T data, String errorCode) {
        this.statusCode = httpStatus.value();
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String errorCode, String message) {
        return new ApiResponse<>(httpStatus, message, null, errorCode);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return of(HttpStatus.CREATED, data);
    }
}
