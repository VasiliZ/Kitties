package com.github.rtyvZ.kitties.network

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    //for success answer from server
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    //error answer from server
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    //something network error
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    // json parsing error and so on
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}