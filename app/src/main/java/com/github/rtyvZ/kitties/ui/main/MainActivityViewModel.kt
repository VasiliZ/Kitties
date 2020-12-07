package com.github.rtyvZ.kitties.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val mutableImagePath = MutableLiveData<String>()
    val getRealPath: LiveData<String> = mutableImagePath

    @Inject
    lateinit var mainRepository: MainRepository

    fun getRealPathForImage(uri: Uri) {
        mutableImagePath.postValue(mainRepository.getPath(uri))
    }
}