package com.github.rtyvZ.kitties.ui.imageCats

import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.network.MyResult
import com.github.rtyvZ.kitties.network.NoConnectivityException
import com.github.rtyvZ.kitties.network.data.Cat
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.VoteCatResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RandomCatsRepository {

    private val checkConnectionProvider = App.ConnectionCheckerProvider
    private val resourcesProvider = App.ResourceProvider
    private val session = App.SessionStorage.getSession()

    suspend fun getCats(): MyResult<List<Cat>?> {
        return withContext(Dispatchers.IO) {
            if (checkConnectionProvider.checkConnection().isNetworkConnected()) {
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
            } else {
                MyResult.Error(NoConnectivityException())
            }
        }
    }

    suspend fun voteForACat(cat: Cat, direction: Int): MyResult<VoteCatResponse?> {
        return withContext(Dispatchers.IO) {
            session?.let {
                if (checkConnectionProvider.checkConnection().isNetworkConnected()) {
                    try {
                        when (direction) {
                            4 -> {
                                val body = Api
                                    .getApi()
                                    .votes(
                                        App.ApiKeyProvider.getKey(),
                                        VoteRequest(cat.id, 0, it.userId)
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
                                        .votes(
                                            App.ApiKeyProvider.getKey(),
                                            VoteRequest(cat.id, 1, it.userId)
                                        )
                                        .execute().body()
                                )
                            else -> {
                                MyResult
                                    .Error(Exception(resourcesProvider.getString(R.string.whats_happens)))
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        MyResult.Error(
                            Exception(
                                resourcesProvider.getString(
                                    R.string.whats_happens
                                )
                            )
                        )
                    }
                } else {
                    MyResult.Error(NoConnectivityException())
                }
            }
        }
            ?: kotlin.run { MyResult.Error(IllegalStateException(resourcesProvider.getString(R.string.no_session))) }
    }

    suspend fun setLikeVote(cat: Cat): MyResult<VoteCatResponse?>? {
        return withContext(Dispatchers.IO) {
            session?.let {
                if (checkConnectionProvider.checkConnection().isNetworkConnected()) {
                    try {
                        MyResult.Success(
                            Api.getApi()
                                .votes(
                                    App.ApiKeyProvider.getKey(),
                                    VoteRequest(cat.id, 1, it.userId)
                                )
                                .execute().body()
                        )
                    } catch (e: Exception) {
                        MyResult.Error(e)
                    }
                } else {
                    MyResult.Error(NoConnectivityException())
                }
            }
        }
    }
}