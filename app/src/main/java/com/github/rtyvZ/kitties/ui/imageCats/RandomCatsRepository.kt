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

    suspend fun voteForACat(cat: Cat, direction: Int): MyResult<VoteCatResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                when (direction) {
                    4 -> {
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
                    8 ->
                        MyResult.Success(
                            Api.getApi()
                                .votes(App.ApiKeyProvider.getKey(), VoteRequest(cat.id, 1))
                                .execute().body()
                        )
                    else -> {
                        MyResult.Error(java.lang.Exception("sdas"))
                    }
                }
            } catch (e: java.lang.Exception) {
                MyResult.Error(java.lang.Exception("sdas"))
            }
        }
    }
}