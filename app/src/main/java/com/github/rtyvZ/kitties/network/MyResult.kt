package com.github.rtyvZ.kitties.network

sealed class MyResult<out R> {
    data class Success<out T>(val data: T) : MyResult<T>()
    data class Error(val exception: Throwable) : MyResult<Nothing>()
}