package com.github.rtyvZ.kitties.repositories.RandomCatsRepository

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.request.FavoritesRequest
import com.github.rtyvZ.kitties.network.request.VoteRequest
import com.github.rtyvZ.kitties.network.response.MyVoteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RandomCatsRepository {

    private val checkConnectionProvider = App.ConnectionCheckerProvider
    private val session = App.SessionStorage.getSession()

    fun getKitties(): Flow<List<Cat>>? = flow {
        var getMyVotes: List<MyVoteResponse>? = null
        val listCats = mutableListOf<Cat>()
        val responseCat = Api
            .getApi()
            .getListKitties()

        responseCat?.map {
            listCats.add(it.toCat())
        }

        session?.let {
            getMyVotes =
                Api.getApi().getMyVotes(
                    App.ApiKeyProvider.getKey(), session.userId
                )
        }

        responseCat?.forEachIndexed { index, catResponce ->
            getMyVotes?.forEach { votes ->
                if (catResponce.id == votes.imageId) {
                    listCats[index] = Cat(
                        catResponce.id,
                        catResponce.url,
                        catResponce.width,
                        catResponce.height,
                        votes.voteValue,
                        votes.idVote.toInt()
                    )
                }
            }
        }
        emit(listCats)
    }

    fun voteForCat(cat: Cat) = flow {

        session?.let {
            if (checkConnectionProvider.checkConnection().isNetworkConnected()) {

                emit(
                    Api.getApi()
                        .votes(
                            App.ApiKeyProvider.getKey(),
                            VoteRequest(cat.id, cat.choice, it.userId)
                        )
                )
            }
        }
    }

    fun deleteVote(cat: Cat) = flow {
        emit(
            Api.getApi()
                .deleteVote(
                    App.ApiKeyProvider.getKey(),
                    cat.voteId.toString()
                )
        )
    }

    fun addToFavorite(catId: String) = flow {

        session?.let { session ->
            if (checkConnectionProvider.checkConnection().isNetworkConnected()) {

                emit(
                    Api.getApi().addCatToFavorites(
                        App.ApiKeyProvider.getKey(),
                        FavoritesRequest(catId, session.userId)
                    )
                )
            }
        }
    }
}