package com.github.rtyvZ.kitties.ui.breedCats

import javax.inject.Inject

class BreedCatsModel @Inject constructor(private val breedCatsRepository: BreedCatsRepository) {
    fun getBreedCats() =
        breedCatsRepository.getBreedCatsFromNetwork()


}
