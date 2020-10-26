package com.github.rtyvZ.kitties.ui.imageCats

import com.github.rtyvZ.kitties.common.models.Cat
import kotlinx.coroutines.flow.Flow

class RandomCatsModel(private val repository: RandomCatsRepository) {
    fun execute(): Flow<List<Cat>>? {
        return repository.getKitties()
    }
}