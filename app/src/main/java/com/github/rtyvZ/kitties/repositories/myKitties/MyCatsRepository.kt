package com.github.rtyvZ.kitties.repositories.myKitties

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyCatsRepository @Inject constructor(
    sessionStorage: UserInternalStorageContract,
    private val api: Api,
) {
    private val session = sessionStorage.getSession()

    fun deleteCat(idImage: String) = flow {
        session?.let {
            emit(api.deleteUploadedImage(idImage))
        }
    }
}