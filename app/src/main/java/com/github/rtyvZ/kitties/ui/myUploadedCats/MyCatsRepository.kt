package com.github.rtyvZ.kitties.ui.myUploadedCats

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import kotlinx.coroutines.flow.flow

class MyCatsRepository {
    private val session = App.SessionStorage.getSession()
    private val keyProvider = App.ApiKeyProvider.getKey()

    fun deleteCat(idImage: String) = flow {
        session?.let {
            emit(Api.getApi().deleteUploadedImage(keyProvider, idImage))
        }
    }
}