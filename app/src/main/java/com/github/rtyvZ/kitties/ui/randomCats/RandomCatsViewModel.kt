package com.github.rtyvZ.kitties.ui.randomCats

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.domain.randomCat.RandomCatsModel
import com.github.rtyvZ.kitties.extentions.replaceElement
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.data.CatResponse
import com.github.rtyvZ.kitties.network.response.MyVoteResponse
import com.github.rtyvZ.kitties.repositories.randomCatsRepository.KittiesPagingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

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

    val kitties = repo.fetchKitties().cachedIn(viewModelScope)

    @Inject
    lateinit var randomCatsModel: RandomCatsModel

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
                    .catch { e ->
                        mutableErrorActionWithCat.postValue(e)
                    }
                    .collect {
                        when (it) {
                            is NetworkResponse.Success -> {
                                cat.apply {
                                    voteId = it.body.id
                                }
                                changeCat(cat)
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
                                changeCat(cat)
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
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    randomCatsModel.addCatToFavorite(localCat[position].id)
                        .collect {
                            when (it) {
                                is NetworkResponse.Success -> {
                                    removeCat(localCat[position])
                                }
                                is NetworkResponse.ApiError -> {
                                    restoreStateCat(localCat[position])
                                }
                                is NetworkResponse.NetworkError -> {
                                    restoreStateCat(localCat[position])
                                    mutableErrorActionWithCat.postValue(it.error)
                                }
                                is NetworkResponse.UnknownError -> {
                                    restoreStateCat(localCat[position])
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

    private fun removeCat(cat: Cat) {
        val listWithCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listWithCats.addAll(it)
        }
        listWithCats.remove(cat)
        mutableRandomCats.postValue(listWithCats)
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