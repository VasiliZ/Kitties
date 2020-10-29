package com.github.rtyvZ.kitties.ui.sendPhoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SendPhotoViewModel : ViewModel() {
    private val mutableSuccessSendPhoto = MutableLiveData<String>()

    private val sendPhotoModel = SendPhotoRepository()

    val getStateSendPhoto: LiveData<String> = mutableSuccessSendPhoto

    fun sendPhoto(file: File, listener: (Int) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sendPhotoModel.uploadPhoto(file, listener).catch { e ->
                    mutableSuccessSendPhoto.postValue(e.message)
                }.collect {
                    mutableSuccessSendPhoto.postValue(it.message)
                }
            }
        }
    }
}