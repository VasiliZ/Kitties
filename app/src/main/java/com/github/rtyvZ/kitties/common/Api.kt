package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.network.data.Cat
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.VoteCatResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {

    @GET("images/search?limit=10")
    fun getListKitties(): Call<List<Cat>>

    @POST("votes")
    fun votes(
        @Header("x-api-key") apiKey: String,
        @Body voteRequest: VoteRequest
    ): Call<VoteCatResponse>

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"

        private var api: Api? = null

        private fun createApi(): Api {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build()

            val appApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
            api = appApi

            return appApi
        }

        fun getApi() = api ?: createApi()
    }
}