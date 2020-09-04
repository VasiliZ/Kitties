package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.MyVoteResponse
import com.github.rtyvZ.kitties.network.response.UploadCatResponse
import com.github.rtyvZ.kitties.network.response.VoteCatResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {

    @GET("images/search?limit=10")
    fun getListKitties(): Call<List<CatResponse>>

    @POST("votes")
    fun votes(
        @Header("x-api-key") apiKey: String,
        @Body voteRequest: VoteRequest
    ): Call<VoteCatResponse>

    @DELETE("votes/{vote_id}")
    fun deleteVote(
        @Header("x-api-key") apiKey: String,
        @Path("vote_id") id: String
    ): Call<VoteCatResponse>


    @POST("images/upload")
    @Multipart
    fun uploadImage(
        @Header("x-api-key") apiKey: String,
        @Part body: MultipartBody.Part
    ): Call<UploadCatResponse>

    @GET("votes")
    fun getMyVotes(
        @Header("x-api-key") apiKey: String,
        @Query("sub_id") id: String
    ): Call<List<MyVoteResponse>>

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