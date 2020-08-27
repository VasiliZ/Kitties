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
        private set
    var getRandomCatsError: LiveData<Throwable> = mutableRandomCatsError

    private val randomCatsRepository = RandomCatsRepository()

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
        val listWithCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listWithCats.addAll(it)
        }
        listWithCats.remove(cat)
        mutableRandomCats.value = listWithCats
    }

    fun vote(position: Int, direction: Int) {
        viewModelScope.launch {
            mutableRandomCats.value?.let { list ->
                when (val response = randomCatsRepository.voteForACat(list[position], direction)) {
                    is MyResult.Success<VoteCatResponse?> -> {
                        response.data?.let {
                            if (it.message == SUCCESS_RESPONSE) {
                                removeCat(list[position])
                            }
                        }
                    }

                    is MyResult.Error -> {
                        returnTheCat(list[position])
                    }
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

    companion object {
        const val SUCCESS_RESPONSE = "SUCCESS"
    }
}