package com.addiction.global;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int statusCode;
    @Schema(description = "HTTP 상태 이름", example = "OK")
    private final HttpStatus httpStatus;
    @Schema(description = "응답 메시지", example = "OK")
    private final String message;
    @Schema(description = "API별 응답 데이터. 오류 응답이면 null입니다.", nullable = true)
    private final T data;
    @Schema(description = "오류 코드. 성공 응답이면 null입니다.", example = "INVALID_ACCESS_TOKEN", nullable = true)
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
