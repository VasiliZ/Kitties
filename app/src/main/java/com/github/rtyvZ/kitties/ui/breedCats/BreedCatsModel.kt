package com.github.rtyvZ.kitties.ui.breedCats

import javax.inject.Inject

class BreedCatsModel @Inject constructor(private val breedCatsRepository: BreedCatsRepository) {
    fun getBreedCats(page: Int, limit: Int) =
        breedCatsRepository.getBreedCatsFromNetwork(page, limit)

    fun getPhotoCatsForBreeds(idImage: String) =
        breedCatsRepository.getBreedsPhotos(idImage)

}
