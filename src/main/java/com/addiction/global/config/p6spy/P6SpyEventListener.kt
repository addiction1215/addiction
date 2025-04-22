package com.addiction.global.config.p6spy

import com.p6spy.engine.common.ConnectionInformation
import com.p6spy.engine.event.JdbcEventListener
import com.p6spy.engine.spy.P6SpyOptions
import java.sql.SQLException

class P6SpyEventListener : JdbcEventListener() {

    override fun onAfterGetConnection(connectionInformation: ConnectionInformation, e: SQLException?) {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6SpyFormatter::class.java.name
    }
}
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
