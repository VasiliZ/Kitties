package com.github.rtyvZ.kitties.ui.imageCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.network.Result
import com.github.rtyvZ.kitties.network.data.Cat
import kotlinx.coroutines.launch

class RandomCatsViewModel : ViewModel() {

    private var mutableRandomCats = MutableLiveData<List<Cat>?>()
    private var mutableRandomCatsError = MutableLiveData<Throwable>()

    var getRandomCats: LiveData<List<Cat>?> = mutableRandomCats
    var getRandomCatsError: LiveData<Throwable> = mutableRandomCatsError

    private val randomCatsRepository = RandomCatsRepository()

    fun clear() {
        mutableRandomCats = MutableLiveData()
        getRandomCats = mutableRandomCats
    }

    fun getCats() {
        viewModelScope.launch {

            when (val getCats = randomCatsRepository.getCats()) {
                is Result.Success<List<Cat>?> -> {
                    getCats.data.let {
                        mutableRandomCats.postValue(it)
                    }
                }

                is Result.Error -> {
                    mutableRandomCatsError.value = getCats.exception
                }
            }
        }
    }
}