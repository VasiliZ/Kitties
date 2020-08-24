package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.network.data.Cat
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Api {

    @GET("images/search?limit=10")
    fun getListKitties(): Call<List<Cat>>

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"

        private var api: Api? = null

        private fun createApi(): Api {
            val okHttpClient = OkHttpClient
                .Builder()
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