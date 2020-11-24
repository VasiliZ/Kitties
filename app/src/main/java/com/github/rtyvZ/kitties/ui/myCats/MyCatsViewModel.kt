package com.github.rtyvZ.kitties.ui.myCats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCatsViewModel : ViewModel() {

    private val myCatsRepository = MyCatsRepository()
    private val deleteUploadedCats = MutableLiveData<Any?>()
    private val myCatsModel = MyCatsModel(myCatsRepository)
    private val successGetListMyCats = MutableLiveData<List<Cat>>()
    val getMyCats: LiveData<List<Cat>> = successGetListMyCats
    val getStatusDeleteUploadedCat = deleteUploadedCats

    fun getMyCatsFromRemoteApi() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                myCatsModel.getCats().collect {
                    successGetListMyCats.postValue(it)
                }
            }
        }
    }

    fun deleteUploadedCat(idImage: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    myCatsModel.deleteCat(idImage).collect {
                        deleteUploadedCats.postValue(it.toString())
                    }

                } catch (e: Exception) {
                    Log.d("error", e.message.toString())
                }
            }
        }
    }
}
