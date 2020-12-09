package com.github.rtyvZ.kitties.ui.breedCats

import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.Api
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BreedCatsRepository @Inject constructor(private val api: Api) {
    private val key = App.ApiKeyProvider.getKey()
    fun getBreedCatsFromNetwork() = flow {
        emit(api.getBreedCats(key))
    }
}
