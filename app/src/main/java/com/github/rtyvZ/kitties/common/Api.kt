package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.request.FavoritesRequest
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.ApiErrorResponse
import com.github.rtyvZ.kitties.network.response.CatResponseVoteAndFav
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import com.github.rtyvZ.kitties.network.response.MyVoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {

    @GET("images/search?limit=10")
    suspend fun getListKitties(
        @Query("page") page: Int
    ): NetworkResponse<List<CatResponse>, Any>

    @POST("votes")
    suspend fun votes(
        @Body voteRequest: VoteRequest
    ): NetworkResponse<CatResponseVoteAndFav, Any>

    @DELETE("votes/{vote_id}")
    suspend fun deleteVote(
        @Path("vote_id") id: String
    ): NetworkResponse<CatResponseVoteAndFav, Any>

    @POST("images/upload")
    @Multipart
    suspend fun uploadImage(
        @Part body: MultipartBody.Part,
        @Part("sub_id") userUid: RequestBody
    ): NetworkResponse<Cat, ApiErrorResponse>

    @GET("votes")
    suspend fun getMyVotes(
        @Query("sub_id") id: String
    ): NetworkResponse<List<MyVoteResponse>, Any>

    @POST("favourites")
    suspend fun addCatToFavorites(
        @Body body: FavoritesRequest
    ): NetworkResponse<CatResponseVoteAndFav, Any>

    @GET("favourites")
    suspend fun getFavoritesCat(
        @Query("sub_id") subId: String
    ): NetworkResponse<List<FavoriteCatsResponse>, Any>

    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavoriteCat(
        @Path("favourite_id") id: Int
    ): NetworkResponse<Any, Any>

    @DELETE("images/{image_id}")
    suspend fun deleteUploadedImage(
        @Path("image_id") id: String
    ): NetworkResponse<Any, Any>

    @GET("breeds")
    suspend fun getAllCatsBreeds(
        @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): NetworkResponse<List<CatBreed>, Any>
}