package com.github.rtyvZ.kitties.ui.myUploadedCats

import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.db.CatDatabase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyCatsRepository @Inject constructor(
    sessionStorage: UserInternalStorageContract,
    private val api: Api,
    private val db: CatDatabase
) {
    private val session = sessionStorage.getSession()

    fun deleteCat(idImage: String) = flow {
        session?.let {
            emit(api.deleteUploadedImage(idImage))
        }
    }

    fun getSavedCats() =
        db.getCatDao().getAllCats()
}