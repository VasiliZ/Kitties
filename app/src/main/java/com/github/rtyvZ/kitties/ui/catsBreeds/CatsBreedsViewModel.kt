package com.github.rtyvZ.kitties.ui.catsBreeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.rtyvZ.kitties.common.models.CatBreed
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.repositories.catBreeds.PagingBreedsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatsBreedsViewModel @Inject constructor(val breedsRepo: PagingBreedsRepo) : ViewModel() {
    @Inject
    lateinit var breedsModel: BreedsModel


    private val mutableBreedList = MutableLiveData<List<CatBreed>>()
    val listBreeds: LiveData<List<CatBreed>> = mutableBreedList
    val breeds = breedsRepo.fetchBreeds().cachedIn(viewModelScope)
    /*fun getBreeds() {
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
        }*/

}
