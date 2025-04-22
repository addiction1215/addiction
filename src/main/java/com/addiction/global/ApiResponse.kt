package com.addiction.global

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val statusCode: Int,
    val httpStatus: HttpStatus,
    val message: String,
    val data: T?
) {
    companion object {
        fun <T> of(httpStatus: HttpStatus, message: String, data: T?): ApiResponse<T> {
            return ApiResponse(httpStatus.value(), httpStatus, message, data)
        }

        fun <T> of(httpStatus: HttpStatus, data: T?): ApiResponse<T> {
            return of(httpStatus, httpStatus.name, data)
        }

        fun <T> ok(data: T?): ApiResponse<T> {
            return of(HttpStatus.OK, data)
        }

        fun <T> created(data: T?): ApiResponse<T> {
            return of(HttpStatus.CREATED, data)
        }
    }
}
