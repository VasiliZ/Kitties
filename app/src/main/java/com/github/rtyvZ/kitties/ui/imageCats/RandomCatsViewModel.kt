package com.github.rtyvZ.kitties.ui.imageCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.extantions.replaceElement
import com.github.rtyvZ.kitties.network.MyResult
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
            val cat = list[position]
            viewModelScope.launch {
                when (val response = randomCatsRepository.voteForACat(cat, direction)) {
                    is MyResult.Error -> {
                        mutableErrorVoteCats.postValue(response.exception)
                        returnACat(cat, position)
                    }
                    is MyResult.Success -> {
                        response.data?.let {
                            cat.voteId = it.id
                            changeCat(cat)
                        }
                    }
                }
            }
        }
    }

    private fun returnACat(cat: Cat, position: Int) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let { listCats.addAll(it) }
        listCats.add(position, cat)
        mutableRandomCats.postValue(listCats)
    }

    private fun restoreStateCat(cat: Cat) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listCats.addAll(it)
        }
        listCats.find {
            it.id == cat.id
        }?.choice = -1
        mutableRandomCats.postValue(listCats)
    }

    private fun changeCat(cat: Cat) {
        val listCat = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listCat.addAll(it)
        }
        listCat.replaceElement(cat)
        mutableRandomCats.postValue(listCat)
    }

    private fun sendVoteRequest(cat: Cat, choice: StateCatVote) {
        viewModelScope.launch {
            when (val response = randomCatsRepository.voteForCat(cat)) {
                is MyResult.Error -> {
                    response.exception.let {
                        restoreStateCat(cat)
                        mutableErrorVoteCats.postValue(it)
                    }
                }
                is MyResult.Success -> {
                    response.data?.let {
                        cat.apply {
                            voteId = it.id
                        }
                        changeCat(cat)
                    }
                }
            }
        }
    }

    private fun sendDeleteVoteRequest(cat: Cat) {
        viewModelScope.launch {
            when (val response = randomCatsRepository.deleteVote(cat)) {
                is MyResult.Error -> {
                    mutableErrorVoteCats.postValue(response.exception)
                }

                is MyResult.Success -> {
                    cat.choice = -1
                    changeCat(cat)
                }
            }
        }
    }

    fun voteForCat(cat: Cat, choice: StateCatVote) {
        when (choice) {
            StateCatVote.LIKE -> {
                if (cat.choice == LIKE) {
                    cat.choice = WITHOUT_VOTE
                    changeCat(cat)
                    sendDeleteVoteRequest(cat)
                } else {
                    cat.choice = LIKE
                    sendVoteRequest(cat, choice)
                    changeCat(cat)
                }
            }

            StateCatVote.DISLIKE -> {
                if (cat.choice == DISLIKE) {
                    cat.choice = WITHOUT_VOTE
                    changeCat(cat)
                    sendDeleteVoteRequest(cat)
                } else {
                    cat.choice = DISLIKE
                    sendVoteRequest(cat, choice)
                    changeCat(cat)
                }
            }
        }
    }

    fun deleteVote(cat: Cat) {
        changeCat(cat)
    }

    companion object {
        const val LIKE = 1
        const val DISLIKE = 0
        const val WITHOUT_VOTE = -1
    }
}
