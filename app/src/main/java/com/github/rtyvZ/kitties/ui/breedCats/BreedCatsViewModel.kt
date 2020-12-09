package com.github.rtyvZ.kitties.ui.breedCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val getBreedsCatsLiveData: LiveData<List<BreedCats>> = mutableBreedCatsLiveData
    val getErrorBreedsCatsLiveData: LiveData<Throwable> = mutableErrorBreedCatsLiveData
    fun getBreedCats() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                viewModelModel.getBreedCats().collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            mutableBreedCatsLiveData.postValue(it.body)
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
}
