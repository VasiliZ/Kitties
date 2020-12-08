package com.github.rtyvZ.kitties.ui.myUploadedCats

import javax.inject.Inject

class MyCatsModel @Inject constructor(private val myCatsRepository: MyCatsRepository) {

    fun deleteCat(idImage: String) =
        myCatsRepository.deleteCat(idImage)

    fun getLocalCats() =
        myCatsRepository.getSavedCats()
}