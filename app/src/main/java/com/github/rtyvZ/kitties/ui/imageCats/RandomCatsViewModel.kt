package com.github.rtyvZ.kitties.ui.imageCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.network.MyResult
import com.github.rtyvZ.kitties.network.data.Cat
import com.github.rtyvZ.kitties.network.response.VoteCatResponse
import kotlinx.coroutines.launch

class RandomCatsViewModel : ViewModel() {

    private var mutableRandomCats = MutableLiveData<List<Cat>?>()
    private var mutableRandomCatsError = MutableLiveData<Throwable>()

    var getRandomCats: LiveData<List<Cat>?> = mutableRandomCats
    var getRandomCatsError: LiveData<Throwable> = mutableRandomCatsError

    private val randomCatsRepository = RandomCatsRepository()

    private val removeCat: (Cat) -> Unit = {
        removeCat(it)
    }

    fun clear() {
        mutableRandomCats = MutableLiveData()
        getRandomCats = mutableRandomCats
    }

    fun getCats() {
        viewModelScope.launch {

            when (val getCats = randomCatsRepository.getCats()) {
                is MyResult.Success<List<Cat>?> -> {
                    getCats.data.let {
                        mutableRandomCats.postValue(it)
                    }
                }

                is MyResult.Error -> {
                    mutableRandomCatsError.value = getCats.exception
                }
            }
        }
    }

    private fun removeCat(cat: Cat) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let { listCats.addAll(it) }
        listCats.remove(cat)
        mutableRandomCats.postValue(listCats)
    }

    fun vote(cat: Cat, direction: StateSwipe) {
        viewModelScope.launch {
            when (val response = randomCatsRepository.voteForACat(cat, direction)) {
                is MyResult.Success<VoteCatResponse?> -> {
                    response.data?.let {
                        if (it.message == SUCCESS_RESPONSE) {
                            removeCat(cat)
                        }
                    }
                }

                is MyResult.Error -> {
                    returnTheCat(cat)
                }
            }
        }
    }

    private fun returnTheCat(tempCat: Cat) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let { listCats.addAll(it) }
        listCats.add(tempCat)
        mutableRandomCats.postValue(listCats)
    }

    fun addToFavorite() {

    }

    companion object {
        const val SUCCESS_RESPONSE = "SUCCESS"
    }
}