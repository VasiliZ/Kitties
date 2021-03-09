package com.github.rtyvZ.kitties.repositories.randomCatsRepository

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.request.FavoritesRequest
import com.github.rtyvZ.kitties.network.request.VoteRequest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RandomCatsRepository @Inject constructor(
    private val sessionStorage: UserInternalStorageContract,
    private val api: Api
) {

    private val session = sessionStorage.getSession()



    fun voteForCat(cat: Cat) = flow {
        session?.let {
            emit(
                api
                    .votes(
                        VoteRequest(cat.id, cat.choice, it.userId)
                    )
            )
        }
    }

    fun deleteVote(cat: Cat) = flow {
        emit(
            api
                .deleteVote(
                    cat.voteId.toString()
                )
        )
    }

    fun addToFavorite(catId: String) = flow {
        session?.let { session ->
            emit(
                api.addCatToFavorites(
                    FavoritesRequest(catId, session.userId)
                )
            )
        }
    }
}