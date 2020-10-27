package com.github.rtyvZ.kitties.repositories.favoriteCats

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import kotlinx.coroutines.flow.flow

class FavoriteCatsRepository {
    private val session = App.SessionStorage.getSession()
    private val keyProvider = App.ApiKeyProvider.getKey()

    fun getFavoriteCats() = flow {
        session?.let {
            emit(Api.getApi().getFavoritesCat(keyProvider, it.userId))
        }
    }

    fun deleteFavoriteCat(catId: Int) = flow {
        session?.let {
            emit(
                Api
                    .getApi()
                    .deleteFavoriteCat(keyProvider, catId)
            )
        }
    }
}
