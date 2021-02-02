package com.github.rtyvZ.kitties.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.inlineFun.checkNull
import com.github.rtyvZ.kitties.common.models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchViewModel @Inject constructor(private val launchModel: LaunchModel) : ViewModel() {

    private val launchSuccess = MutableLiveData<String>()
    private val launchError = MutableLiveData<Throwable>()
    private val mutableApiKey = MutableLiveData<String>()

    val getUserUid: LiveData<String> = launchSuccess
    val error: LiveData<Throwable> = launchError

    fun getDataForAuth() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val channel = Channel<String>()
                val channelFromKey = Channel<String>()
                launchModel.getUserUid(channel)
                launchModel.getKeyFromFirestore(channelFromKey)
                val key: String?
                val userUid: String?
                try {
                    channel.receive().also {
                        launchModel.saveUserUid(it)
                        userUid = it
                    }

                    channelFromKey.receive().also {
                        mutableApiKey.postValue(it)
                        launchModel.encryptKey(it)
                        key = it
                    }

                    checkNull(key, userUid, launchError) { _, userId ->
                        launchSuccess.postValue(userId)
                    }

                } catch (e: Exception) {
                    launchError.postValue(e)
                }
            }
        }
    }
}