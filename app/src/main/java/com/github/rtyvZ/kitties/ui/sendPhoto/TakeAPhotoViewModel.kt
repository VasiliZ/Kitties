package com.github.rtyvZ.kitties.ui.sendPhoto

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TakeAPhotoViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var takeAPhotoRepository: TakeAPhotoRepository
}
