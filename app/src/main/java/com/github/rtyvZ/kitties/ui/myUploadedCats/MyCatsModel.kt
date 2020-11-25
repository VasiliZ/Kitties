package com.github.rtyvZ.kitties.ui.myUploadedCats

class MyCatsModel(private val myCatsRepository: MyCatsRepository) {

    fun getCats() =
        myCatsRepository.getUploadedCats()

    fun deleteCat(idImage: String) =
        myCatsRepository.deleteCat(idImage)
}