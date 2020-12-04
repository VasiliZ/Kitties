package com.github.rtyvZ.kitties.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchViewModel @Inject constructor(val launchModel: LaunchModel) : ViewModel() {

    private val launchSuccess = MutableLiveData<String>()
    private val launchError = MutableLiveData<Throwable>()

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