package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.request.FavoritesRequest
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import com.github.rtyvZ.kitties.network.response.MyVoteResponse
import com.github.rtyvZ.kitties.network.response.UploadCatResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {

    @GET("images/search?limit=10")
    suspend fun getListKitties(): List<CatResponse>?

    @POST("votes")
    suspend fun votes(
        @Header("x-api-key") apiKey: String,
        @Body voteRequest: VoteRequest
    ): CatResponseVoteAndFav

    @DELETE("votes/{vote_id}")
    suspend fun deleteVote(
        @Header("x-api-key") apiKey: String,
        @Path("vote_id") id: String
    ): CatResponseVoteAndFav

    @POST("images/upload")
    @Multipart
    suspend fun uploadImage(
        @Header("x-api-key") apiKey: String,
        @Part body: MultipartBody.Part,
        @Part("sub_id") userUid: RequestBody
    ): UploadCatResponse

    @GET("votes")
    suspend fun getMyVotes(
        @Header("x-api-key") apiKey: String,
        @Query("sub_id") id: String
    ): List<MyVoteResponse>?

    @POST("favourites")
    suspend fun addCatToFavorites(
        @Header("x-api-key") apiKey: String,
        @Body body: FavoritesRequest
    ): CatResponseVoteAndFav

    @GET("favourites")
    suspend fun getFavoritesCat(
        @Header("x-api-key") apiKey: String,
        @Query("sub_id") subId: String
    ): List<FavoriteCatsResponse>

    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavoriteCat(
        @Header("x-api-key") apiKey: String,
        @Path("favourite_id") id: Int
    ): Any

    @GET("images")
    suspend fun getUploadedCats(
        @Header("x-api-key") apiKey: String,
        @Query("sub_id") userId: String,
        @Query("limit") limitCats: Int
    ): List<Cat>

    @DELETE("images/{image_id}")
    suspend fun deleteUploadedImage(
        @Header("x-api-key") apiKey: String,
        @Path("image_id") id: String
    )

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"

        private var api: Api? = null

        private fun createApi(): Api {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .addInterceptor(ErrorInterceptor())
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