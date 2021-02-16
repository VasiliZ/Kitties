package com.github.rtyvZ.kitties.ui.catsBreeds

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BreedsRepository @Inject constructor(
    private val sessionStorage: UserInternalStorageContract,
    private val api: Api
) {

    private val session = sessionStorage.getSession()
    fun getAllBreeds() = flow {
        session?.let {
            emit(api.getAllCatsBreeds())
        }
    }
}

