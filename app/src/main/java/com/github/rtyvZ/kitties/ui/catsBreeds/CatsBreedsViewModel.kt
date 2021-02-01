package com.github.rtyvZ.kitties.ui.catsBreeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatsBreedsViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var breedsModel: BreedsModel

    private val mutableBreedList = MutableLiveData<List<CatBreed>>()
    val listBreeds: LiveData<List<CatBreed>> = mutableBreedList

    fun getBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                breedsModel.getBreeds().collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            mutableBreedList.postValue(it.body)
                        }
                        is NetworkResponse.ApiError -> {
//todo
                        }

                        is NetworkResponse.NetworkError -> {
//todo
                        }

                        is NetworkResponse.UnknownError -> {
                            //todo
                        }
                    }
                }
            }
        }
    }
}
