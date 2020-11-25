package com.github.rtyvZ.kitties.ui.myUploadedCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCatsViewModel : ViewModel() {

    private val myCatsRepository = MyCatsRepository()
    private val myCatsModel = MyCatsModel(myCatsRepository)
    private val successGetListMyCats = MutableLiveData<List<Cat>>()
    private val errorWhileGetsMyCats = MutableLiveData<Throwable>()
    private val errorDeletingMyCats = MutableLiveData<Throwable>()
    val getMyCats: LiveData<List<Cat>> = successGetListMyCats
    val errorWhileDeletingCat = errorDeletingMyCats
    val getErrorMyCats = errorWhileGetsMyCats

    fun getMyCatsFromRemoteApi() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                myCatsModel.getCats().collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            successGetListMyCats.postValue(it.body)
                        }
                        is NetworkResponse.UnknownError -> {
                            it.error?.let { error ->
                                errorWhileGetsMyCats.postValue(error)
                            }
                        }
                        is NetworkResponse.ApiError -> {
                        }
                        is NetworkResponse.NetworkError -> {
                            errorWhileGetsMyCats.postValue(it.error)
                        }
                    }
                }
            }
        }
    }

    fun deleteUploadedCat(cat: Cat, position: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                myCatsModel.deleteCat(cat.id).collect {
                    deleteMyCat(cat)
                    when (it) {
                        is NetworkResponse.Success -> {

                        }
                        is NetworkResponse.NetworkError -> {
                            restoreCat(cat, position)
                            errorDeletingMyCats.postValue(it.error)
                        }
                        is NetworkResponse.ApiError -> {

                        }
                        is NetworkResponse.UnknownError -> {
                            it.error?.let { error ->
                                errorDeletingMyCats.postValue(error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deleteMyCat(cat: Cat) {
        val listCats = mutableListOf<Cat>()
        successGetListMyCats.value?.let {
            listCats.addAll(it)
        }
        listCats.remove(cat)
        successGetListMyCats.postValue(listCats)
    }

    private fun restoreCat(cat: Cat, position: Int) {
        val listCats = mutableListOf<Cat>()
        successGetListMyCats.value?.let {
            listCats.addAll(it)
        }
        listCats.add(position, cat)
        successGetListMyCats.postValue(listCats)
    }
}
