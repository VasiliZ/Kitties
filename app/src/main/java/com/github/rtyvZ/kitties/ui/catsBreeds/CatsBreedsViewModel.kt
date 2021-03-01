package com.github.rtyvZ.kitties.ui.catsBreeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.rtyvZ.kitties.repositories.catBreeds.PagingBreedsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatsBreedsViewModel @Inject constructor(private val breedsRepo: PagingBreedsRepo) : ViewModel() {
    @Inject
    lateinit var breedsModel: BreedsModel

    var breeds = breedsRepo.fetchBreeds().cachedIn(viewModelScope)

    fun getMoreData(){
        breeds = breedsRepo.fetchBreeds().cachedIn(viewModelScope)
    }
}
