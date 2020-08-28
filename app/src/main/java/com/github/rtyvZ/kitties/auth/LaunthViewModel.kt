package com.github.rtyvZ.kitties.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.network.MyResult
import kotlinx.coroutines.launch

class LaunchViewModel : ViewModel() {

    private val launchSuccess = MutableLiveData<String>()
    private val launchError = MutableLiveData<Throwable>()
    private val launchModel = LaunchModel()

    val getUserUid: LiveData<String> = launchSuccess
    val error: LiveData<Throwable> = launchError

    private val resultCallback: (MyResult<String>) -> Unit = { result ->
        when (result) {
            is MyResult.Success -> {
                result.data.let {
                    launchSuccess.postValue(it)
                }
            }

            is MyResult.Error -> {
                launchError.postValue(result.exception)
            }
        }
    }

    fun requestUid() {
        viewModelScope.launch {
            launchModel.getUserUid(resultCallback)
        }
    }
}