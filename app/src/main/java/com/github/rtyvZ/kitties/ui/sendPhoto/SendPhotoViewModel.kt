package com.github.rtyvZ.kitties.ui.sendPhoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.network.MyResult
import kotlinx.coroutines.launch
import java.io.File

class SendPhotoViewModel : ViewModel() {
    private val mutableSuccessSendPhoto = MutableLiveData<String>()

    private val sendPhotoModel = SendPhotoModel()

    val getStateSendPhoto: LiveData<String> = mutableSuccessSendPhoto

    fun sendPhoto(file: File, listener: (Int) -> Unit) {
        viewModelScope.launch {

            when (val answer = sendPhotoModel.uploadPhoto(file, listener)) {
                is MyResult.Success -> {
                    answer.data?.let {
                        mutableSuccessSendPhoto.postValue(it.message)
                    } ?: kotlin.run {
                        mutableSuccessSendPhoto.postValue("500")
                    }
                }
            }
        }
    }
}