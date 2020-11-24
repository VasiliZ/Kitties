package com.github.rtyvZ.kitties.ui.myCats

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import kotlinx.coroutines.flow.flow

class MyCatsRepository {
    private val session = App.SessionStorage.getSession()
    private val keyProvider = App.ApiKeyProvider.getKey()

    fun getUploadedCats() = flow {
        session?.let {
            emit(
                Api
                    .getApi()
                    .getUploadedCats(keyProvider, session.userId, LIMIT_CATS)
            )
        }
    }

    fun deleteCat(idImage: String) = flow {
        session?.let {
            emit(Api.getApi().deleteUploadedImage(keyProvider, idImage))
        }
    }

    companion object {
        const val LIMIT_CATS = 100
    }
}