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
    private var mutableErrorVoteCats = MutableLiveData<Throwable>()

    var getRandomCats: LiveData<List<Cat>?> = mutableRandomCats
        private set
    var getRandomCatsError: LiveData<Throwable> = mutableRandomCatsError
        private set
    var getErrorVoteCat: LiveData<Throwable> = mutableErrorVoteCats

    private val randomCatsRepository = RandomCatsRepository()

    fun clear() {
        mutableRandomCats = MutableLiveData()
        getRandomCats = mutableRandomCats
        mutableRandomCatsError = MutableLiveData()
        getRandomCatsError = mutableRandomCatsError
        mutableErrorVoteCats = MutableLiveData()
        getErrorVoteCat = mutableErrorVoteCats
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
        mutableRandomCats.value?.let { list ->
            removeCat(list[position])
            viewModelScope.launch {
                when (val response = randomCatsRepository.voteForACat(list[position], direction)) {
                    is MyResult.Success<VoteCatResponse?> -> {
                        response.data?.let {
                            if (it.message == SUCCESS_RESPONSE) {
                                // removeCat(list[position])
                            }
                        }
                    }

                    is MyResult.Error -> {
                        mutableErrorVoteCats.postValue(response.exception)
                        returnACat(list[position], position)
                    }
                }
            }
        }
    }

    private fun returnACat(tempCat: Cat, position: Int) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let { listCats.addAll(it) }
        listCats.add(position, tempCat)
        mutableRandomCats.postValue(listCats)
    }

    private fun restoreStateCat(cat: Cat) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listCats.addAll(it)
        }
        listCats.find {
            it.id == cat.id
        }?.isSetLike = !cat.isSetLike
        mutableRandomCats.postValue(listCats)
    }

    private fun changeCat(cat: Cat) {
        val listCat = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listCat.addAll(it)
        }
        listCat.find {
            it.id == cat.id
        }?.isSetLike = cat.isSetLike
        mutableRandomCats.postValue(listCat)
    }

    fun setLike(cat: Cat) {
        changeCat(cat)
        viewModelScope.launch {
            when (val response = randomCatsRepository.setLikeVote(cat)) {
                is MyResult.Error -> {
                    response.exception.let {
                        restoreStateCat(cat)
                        mutableErrorVoteCats.postValue(it)
                    }
                }

                is MyResult.Success -> {

                }
            }
        }
    }

    fun deleteVote(cat: Cat) {
        changeCat(cat)
    }

    companion object {
        const val SUCCESS_RESPONSE = "SUCCESS"
    }
}