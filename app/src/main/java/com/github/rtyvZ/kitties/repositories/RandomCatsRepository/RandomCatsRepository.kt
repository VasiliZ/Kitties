package com.github.rtyvZ.kitties.repositories.RandomCatsRepository

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.request.FavoritesRequest
import com.github.rtyvZ.kitties.network.request.VoteRequest
import kotlinx.coroutines.flow.flow

class RandomCatsRepository {

    private val session = App.SessionStorage.getSession()

    fun getKitties() = flow {
        val responseRandomCats = Api
            .getApi()
            .getListKitties()
        emit(responseRandomCats)
    }

    fun getVotes() = flow {
        session?.let {
            val responseVotes =
                Api.getApi().getMyVotes(
                    App.ApiKeyProvider.getKey(), session.userId
                )
            emit(responseVotes)
        }
    }

    fun voteForCat(cat: Cat) = flow {

        session?.let {
            emit(
                Api.getApi()
                    .votes(
                        App.ApiKeyProvider.getKey(),
                        VoteRequest(cat.id, cat.choice, it.userId)
                    )
            )
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
            emit(
                Api.getApi().addCatToFavorites(
                    App.ApiKeyProvider.getKey(),
                    FavoritesRequest(catId, session.userId)
                )
            )
        }
    }
}