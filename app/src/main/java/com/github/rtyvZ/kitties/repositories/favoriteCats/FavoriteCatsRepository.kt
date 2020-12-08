package com.github.rtyvZ.kitties.repositories.favoriteCats

import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoriteCatsRepository @Inject constructor(sessionStorage: UserInternalStorageContract) {
    private val session = sessionStorage.getSession()
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
