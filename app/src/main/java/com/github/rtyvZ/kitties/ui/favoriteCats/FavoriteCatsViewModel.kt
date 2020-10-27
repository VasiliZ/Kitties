package com.github.rtyvZ.kitties.ui.favoriteCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.domain.favoriteCats.FavoriteCatsModel
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteCatsViewModel : ViewModel() {

    private val mutableFavoriteCatList = MutableLiveData<List<FavoriteCatsResponse>>()
    private val favoriteCatsRepository = FavoriteCatsRepository()
    private val favoriteCatsModel = FavoriteCatsModel(favoriteCatsRepository)
    val getMyFavoriteCats: LiveData<List<FavoriteCatsResponse>> = mutableFavoriteCatList

    fun getFavoriteCats() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteCatsModel.getFavoriteCats().collect {
                    mutableFavoriteCatList.postValue(it)
                }
            }
        }
    }

    fun deleteFavoriteCat(position: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mutableFavoriteCatList.value?.let {
                    favoriteCatsModel.deleteFavoriteCat(it[position].id).collect {
                        deleteItem(position)
                    }
                }
            }
        }
    }

    private fun deleteItem(index: Int) {
        val catList = mutableListOf<FavoriteCatsResponse>()
        mutableFavoriteCatList.value?.let {
            catList.addAll(it)
        }
        catList.removeAt(index)
        mutableFavoriteCatList.postValue(catList)
    }
}