package com.github.rtyvZ.kitties.ui.imageCats

import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.network.MyResult
import com.github.rtyvZ.kitties.network.data.Cat
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.VoteCatResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RandomCatsRepository {

    suspend fun getCats(): MyResult<List<Cat>?> {
        return withContext(Dispatchers.IO) {
            try {
                MyResult.Success(
                    Api
                        .getApi()
                        .getListKitties()
                        .execute()
                        .body()
                )
            } catch (e: Exception) {
                MyResult.Error(e)
            }
        }
    }

    suspend fun voteForACat(cat: Cat, direction: StateSwipe): MyResult<VoteCatResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                when (direction) {
                    StateSwipe.LIKE -> {
                        val body = Api
                            .getApi()
                            .votes(
                                App.ApiKeyProvider.getKey(), VoteRequest(cat.id, 0)
                            )
                            .execute()
                            .body()

                        MyResult
                            .Success(
                                body
                            )
                    }
                    StateSwipe.DISLIKE ->
                        MyResult.Success(
                            Api.getApi()
                                .votes(App.ApiKeyProvider.getKey(), VoteRequest(cat.id, 1))
                                .execute().body()
                        )
                    else -> MyResult.Error(IllegalStateException("Wrong direction"))
                }
            } catch (e: java.lang.Exception) {
                MyResult.Error(e)
            }
        }
    }
}