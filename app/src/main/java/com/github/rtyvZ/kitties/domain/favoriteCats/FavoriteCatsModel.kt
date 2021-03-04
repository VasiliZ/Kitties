package com.github.rtyvZ.kitties.domain.favoriteCats

import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsRepository
import javax.inject.Inject

class FavoriteCatsModel @Inject constructor(private val favoriteCatsRepository: FavoriteCatsRepository) {
    fun deleteFavoriteCat(catId: Int) =
        favoriteCatsRepository.deleteFavoriteCat(catId)

}
