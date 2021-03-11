package com.github.rtyvZ.kitties.ui.randomCats

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.domain.randomCat.RandomCatsModel
import com.github.rtyvZ.kitties.extentions.replaceElement
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.repositories.randomCatsRepository.KittiesPagingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RandomCatsViewModel @Inject constructor(repo: KittiesPagingRepo) : ViewModel() {

    private var mutableRandomCats = MutableLiveData<List<Cat>?>()
    private var mutableRandomCatsError = MutableLiveData<Throwable>()
    private var mutableErrorActionWithCat = MutableLiveData<Throwable>()

    var getRandomCats: LiveData<List<Cat>?> = mutableRandomCats
        private set
    var getRandomCatsError: LiveData<Throwable> = mutableRandomCatsError
        private set
    var getErrorActionWithCat: LiveData<Throwable> = mutableErrorActionWithCat

    private val kitties = repo.fetchKitties().cachedIn(viewModelScope).asLiveData().let {
        it as MutableLiveData<PagingData<Cat>>
    }

    @Inject
    lateinit var randomCatsModel: RandomCatsModel
    val listKitties: LiveData<PagingData<Cat>> = kitties

    fun clear() {
        mutableRandomCats = MutableLiveData()
        getRandomCats = mutableRandomCats
        mutableRandomCatsError = MutableLiveData()
        getRandomCatsError = mutableRandomCatsError
        mutableErrorActionWithCat = MutableLiveData()
        getErrorActionWithCat = mutableErrorActionWithCat
    }

    private fun sendVoteRequest(cat: Cat) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                randomCatsModel.voteForCat(cat)
                    .collect {
                        when (it) {
                            is NetworkResponse.Success -> {
                                cat.apply {
                                    voteId = it.body.id
                                }
                            }
                            is NetworkResponse.ApiError -> {
                                restoreStateCat(cat)
                            }
                            is NetworkResponse.NetworkError -> {
                                restoreStateCat(cat)
                                mutableErrorActionWithCat.postValue(it.error)
                            }
                            is NetworkResponse.UnknownError -> {
                                it.error?.let { throwable ->
                                    restoreStateCat(cat)
                                    mutableErrorActionWithCat.postValue(throwable)
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun sendDeleteVoteRequest(cat: Cat) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                randomCatsModel.deleteVoteForCat(cat)
                    .collect {
                        when (it) {
                            is NetworkResponse.Success -> {
                                cat.choice = -1
                            }
                            is NetworkResponse.UnknownError -> {
                                it.error?.let { throwable ->
                                    mutableErrorActionWithCat.postValue(throwable)
                                }
                            }
                            is NetworkResponse.NetworkError -> {
                                restoreStateCat(cat)
                                mutableErrorActionWithCat.postValue(it.error)
                            }
                            is NetworkResponse.ApiError -> {
                                restoreStateCat(cat)
                            }
                        }

                    }
            }
        }
    }

    fun voteForCat(cat: Cat, choice: StateCatVote) {
        val data = kitties.value ?: return

        when (choice) {
            StateCatVote.LIKE -> {
                if (cat.choice == LIKE) {
                    changeVote(data, cat, WITHOUT_VOTE) {
                        sendDeleteVoteRequest(it)
                    }

                } else {
                    changeVote(data, cat, LIKE) {
                        sendVoteRequest(it)
                    }
                }
            }

            StateCatVote.DISLIKE -> {
                if (cat.choice == DISLIKE) {
                    changeVote(data, cat, WITHOUT_VOTE) {
                        sendDeleteVoteRequest(it)
                    }

                } else {
                    changeVote(data, cat, DISLIKE) {
                        sendVoteRequest(it)
                    }
                }
            }
        }
    }

    private fun changeVote(data: PagingData<Cat>, cat: Cat, choice: Int, request: (Cat) -> Unit) {
        data.map {
            if (it.id == cat.id) {
                val changeDCat = cat.copy(choice = choice)
                request.invoke(changeDCat)
                return@map changeDCat
            } else return@map it
        }
            .let {
                kitties.postValue(it)
            }
    }

    fun addToFavorites(cat: Cat?) {
        val data = kitties.value ?: return
        cat?.let { kitty ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    randomCatsModel.addCatToFavorite(kitty.id)
                        .collect {
                            when (it) {
                                is NetworkResponse.Success -> {
                                    data.filter {
                                        it.id != kitty.id
                                    }.let {
                                        kitties.postValue(it)
                                    }
                                }
                                is NetworkResponse.ApiError -> {
                                    data.let {
                                        kitties.postValue(it)
                                    }
                                }
                                is NetworkResponse.NetworkError -> {
                                    data.let {
                                        kitties.postValue(it)
                                    }
                                    mutableErrorActionWithCat.postValue(it.error)
                                }
                                is NetworkResponse.UnknownError -> {
                                    data.let {
                                        kitties.postValue(it)
                                    }
                                    it.error?.let { throwable ->
                                        mutableErrorActionWithCat.postValue(throwable)
                                    }
                                }
                            }
                        }
                }
            }
        }
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

    companion object {
        const val LIKE = 1
        const val DISLIKE = 0
        const val WITHOUT_VOTE = -1
    }
}