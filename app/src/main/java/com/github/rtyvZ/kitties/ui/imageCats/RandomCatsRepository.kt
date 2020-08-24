package com.github.rtyvZ.kitties.ui.imageCats

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.network.Result
import com.github.rtyvZ.kitties.network.data.Cat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RandomCatsRepository {

    suspend fun getCats(): Result<List<Cat>?> {
        return withContext(Dispatchers.Default) {
            try {
                Result.Success(
                    Api
                        .getApi()
                        .getListKitties()
                        .execute()
                        .body()
                )
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}