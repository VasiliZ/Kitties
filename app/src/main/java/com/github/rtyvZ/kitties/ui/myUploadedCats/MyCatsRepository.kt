package com.github.rtyvZ.kitties.ui.myUploadedCats

import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyCatsRepository @Inject constructor(sessionStorage: UserInternalStorageContract) {
    private val session = sessionStorage.getSession()
    private val keyProvider = App.ApiKeyProvider.getKey()

    fun deleteCat(idImage: String) = flow {
        session?.let {
            emit(Api.getApi().deleteUploadedImage(keyProvider, idImage))
        }
    }

    fun getSavedCats() =
        App.DataBaseProvider.getDataBase().getCatDao().getAllCats()

}