package com.jar.demo.network

/**
 * Sealed class to use as a wrapper for response and error.
 * It's serves purpose as enums as well as stores data to send it back to caller.
 */
sealed class ApiResponseWrapper<out T> {
    data class Success<out T>(val value: T) : ApiResponseWrapper<T>()
    data class Error(val code: Int?, val errorMessage: String?) :
        ApiResponseWrapper<Nothing>()

    object NetworkError : ApiResponseWrapper<Nothing>()
    object TimeOutError : ApiResponseWrapper<Nothing>()
}
