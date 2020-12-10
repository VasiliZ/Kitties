package com.github.rtyvZ.kitties.ui.breedCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.extentions.replaceElement
import com.github.rtyvZ.kitties.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedCatsViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var viewModelModel: BreedCatsModel

    private val mutableBreedCatsLiveData = MutableLiveData<List<BreedCats>>()
    private val mutableErrorBreedCatsLiveData = MutableLiveData<Throwable>()
    private val limit = 10

    val getBreedsCatsLiveData: LiveData<List<BreedCats>> = mutableBreedCatsLiveData
    val getErrorBreedsCatsLiveData: LiveData<Throwable> = mutableErrorBreedCatsLiveData

    fun getBreedCats(page: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                viewModelModel.getBreedCats(page, limit).collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            val mutableListWithBreeds = mutableListOf<BreedCats>()
                            mutableBreedCatsLiveData.value?.let { listBreads ->
                                mutableListWithBreeds.addAll(listBreads)
                            }
                            mutableListWithBreeds.addAll(it.body)
                            mutableListWithBreeds.forEach { breed ->
                                breed.imageId?.let { imageId ->
                                    viewModelModel.getPhotoCatsForBreeds(imageId).collect { cat ->
                                        if (cat is NetworkResponse.Success) {
                                            breed.url = cat.body.url
                                        }
                                    }
                                }
                            }
                            mutableBreedCatsLiveData.postValue(mutableListWithBreeds)
                        }
                        is NetworkResponse.NetworkError -> {
                            mutableErrorBreedCatsLiveData.postValue(it.error)
                        }
                        is NetworkResponse.ApiError -> {
                            //this api doesn't provide api errors :(
                        }
                        is NetworkResponse.UnknownError -> {
                            it.error?.let { error ->
                                mutableErrorBreedCatsLiveData.postValue(error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUrlsForBreeds(body: BreedCats) {

    }

    private fun changeBreedItem(breed: BreedCats, body: Cat): List<BreedCats> {
        val listBreeds = mutableListOf<BreedCats>()
        mutableBreedCatsLiveData.value?.let {
            listBreeds.addAll(it)
        }
        breed.url = body.url
        listBreeds.replaceElement(breed)
        return listBreeds
    }
}
