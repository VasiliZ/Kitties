package com.github.rtyvZ.kitties.ui.myCats

import com.github.rtyvZ.kitties.common.models.Cat
import kotlinx.coroutines.flow.Flow

class MyCatsModel(private val myCatsRepository: MyCatsRepository) {

    fun getCats(): Flow<List<Cat>> {
        return myCatsRepository.getUploadedCats()
    }

    fun deleteCat(idImage: String) =
        myCatsRepository.deleteCat(idImage)
}