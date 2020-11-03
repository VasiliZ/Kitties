package com.github.rtyvZ.kitties.ui.imageCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.domain.randomCat.RandomCatsModel
import com.github.rtyvZ.kitties.extentions.replaceElement
import com.github.rtyvZ.kitties.repositories.RandomCatsRepository.RandomCatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private val randomCatsModel = RandomCatsModel(randomCatsRepository)

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
            withContext(Dispatchers.IO) {
                randomCatsModel
                    .getKittiesFromNet()
                    ?.catch { e ->
                        mutableRandomCatsError.postValue(e)
                    }?.collect {
                        val listCatsForEndlessList = mutableListOf<Cat>()
                        mutableRandomCats.value?.let { oldList ->
                            listCatsForEndlessList.addAll(oldList)
                        }
                        listCatsForEndlessList.addAll(it)
                        mutableRandomCats.postValue(listCatsForEndlessList)
                    }
            }
        }
    }


    private fun sendVoteRequest(cat: Cat) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                randomCatsModel.voteForCat(cat)
                    .catch { e ->
                        mutableErrorVoteCats.postValue(e)
                    }
                    .collect {
                        cat.apply {
                            voteId = it.id
                        }
                        changeCat(cat)
                    }
            }
        }
    }

    private fun sendDeleteVoteRequest(cat: Cat) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                randomCatsModel.deleteVoteForCat(cat)
                    .catch { e ->
                        mutableErrorVoteCats.postValue(e)
                    }
                    .collect {
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
                    sendVoteRequest(cat)
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
                    sendVoteRequest(cat)
                    changeCat(cat)
                }
            }
        }
    }

    fun addToFavorites(position: Int) {
        mutableRandomCats.value?.let { localCat ->
            removeCat(localCat[position])
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    randomCatsModel.addCatToFavorite(localCat[position].id)
                        .catch { e ->
                            restoreStateCat(localCat[position])
                            mutableErrorVoteCats.postValue(e)
                        }
                        .collect {
                            //for now nothing to do here
                        }
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


    companion object {
        const val LIKE = 1
        const val DISLIKE = 0
        const val WITHOUT_VOTE = -1
    }
}
