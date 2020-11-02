package com.github.rtyvZ.kitties.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchViewModel : ViewModel() {

    private val launchSuccess = MutableLiveData<String>()
    private val launchError = MutableLiveData<Throwable>()
    private val launchModel = LaunchModel()

    val getUserUid: LiveData<String> = launchSuccess
    val error: LiveData<Throwable> = launchError

    fun requestUid() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val channel = Channel<String>()
                launchModel.getUserUid(channel)

                try {
                    launchSuccess.postValue(channel.receive())
                } catch (e: Exception) {
                    launchError.postValue(e)
                }

            }
        }
    }
}