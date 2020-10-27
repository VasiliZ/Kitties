package com.github.rtyvZ.kitties.domain.favoriteCats

import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsRepository

class FavoriteCatsModel(private val favoriteCatsRepository: FavoriteCatsRepository) {
    fun getFavoriteCats() = favoriteCatsRepository.getFavoriteCats()
    fun deleteFavoriteCat(catId: Int) =
        favoriteCatsRepository.deleteFavoriteCat(catId)

}
