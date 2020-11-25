package com.github.rtyvZ.kitties.ui.favoriteCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.domain.favoriteCats.FavoriteCatsModel
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteCatsViewModel : ViewModel() {

    private val mutableFavoriteCatList = MutableLiveData<List<FavoriteCatsResponse>>()
    private val receiveErrorFavoriteCats = MutableLiveData<Throwable>()
    private val favoriteCatsRepository = FavoriteCatsRepository()
    private val errorDeleteFavoriteCat = MutableLiveData<Throwable>()
    private val favoriteCatsModel = FavoriteCatsModel(favoriteCatsRepository)
    val getMyFavoriteCats: LiveData<List<FavoriteCatsResponse>> = mutableFavoriteCatList
    val getErrorReceiveCats = receiveErrorFavoriteCats
    val getErrorDeleteFavoriteCats = errorDeleteFavoriteCat

    fun getFavoriteCats() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteCatsModel.getFavoriteCats().collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            mutableFavoriteCatList.postValue(it.body)
                        }
                        is NetworkResponse.NetworkError -> {
                            receiveErrorFavoriteCats.postValue(it.error)
                        }
                        is NetworkResponse.UnknownError -> {
                            it.error?.let { error ->
                                receiveErrorFavoriteCats.postValue(error)
                            }
                        }
                        is NetworkResponse.ApiError -> {
                        }
                    }
                }
            }
        }
    }

    fun deleteFavoriteCat(position: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mutableFavoriteCatList.value?.let {
                    val cat = it[position]
                    deleteItem(cat)
                    favoriteCatsModel
                        .deleteFavoriteCat(it[position].id)
                        .collect { response ->
                            when (response) {
                                is NetworkResponse.Success -> {
                                    //nothing to do here
                                }
                                is NetworkResponse.NetworkError -> {
                                    restoreCat(cat, position)
                                    getErrorDeleteFavoriteCats.postValue(response.error)
                                }
                                is NetworkResponse.ApiError -> {

                                }
                                is NetworkResponse.UnknownError -> {
                                    response.error?.let { error ->
                                        getErrorDeleteFavoriteCats.postValue(error)
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun deleteItem(cat: FavoriteCatsResponse) {
        val catList = mutableListOf<FavoriteCatsResponse>()
        mutableFavoriteCatList.value?.let {
            catList.addAll(it)
        }
        catList.remove(cat)
        mutableFavoriteCatList.postValue(catList)
    }

    private fun restoreCat(cat: FavoriteCatsResponse, position: Int) {
        val listFavCats = mutableListOf<FavoriteCatsResponse>()
        mutableFavoriteCatList.value?.let {
            listFavCats.addAll(it)
        }
        listFavCats.add(position, cat)
        mutableFavoriteCatList.postValue(listFavCats)
    }
}