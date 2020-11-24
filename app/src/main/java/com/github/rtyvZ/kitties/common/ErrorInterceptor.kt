package com.github.rtyvZ.kitties.common

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == CODE_204) {
            return Response.Builder()
                .code(200)
                .body(response.body).protocol(Protocol.HTTP_2)
                .message("SUCCESS")
                .request(request)
                .build()
        }
        return response
    }


    companion object {
        const val CODE_204 = 204
    }
}