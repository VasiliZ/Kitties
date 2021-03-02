package com.github.rtyvZ.kitties.ui.myUploadedCats

import com.github.rtyvZ.kitties.repositories.myKitties.MyCatsRepository
import javax.inject.Inject

class MyCatsModel @Inject constructor(private val myCatsRepository: MyCatsRepository) {

    fun deleteCat(idImage: String) =
        myCatsRepository.deleteCat(idImage)
}