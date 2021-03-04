package com.github.rtyvZ.kitties.ui.favoriteCats

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.github.rtyvZ.kitties.domain.favoriteCats.FavoriteCatsModel
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsPagingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteCatsViewModel @Inject constructor(
    private val repo: FavoriteCatsPagingRepo
) : ViewModel() {

    private val receiveErrorFavoriteCats = MutableLiveData<Throwable>()
    private val errorDeleteFavoriteCat = MutableLiveData<Throwable>()
    private val randomKitties =
        repo.fetchFavoriteKitties()
            .cachedIn(viewModelScope)
            .asLiveData().let {
                it as MutableLiveData<PagingData<FavoriteCatsResponse>>
            }

    @Inject
    lateinit var favoriteCatsModel: FavoriteCatsModel
    var fetchKitties: LiveData<PagingData<FavoriteCatsResponse>> = randomKitties

    val getErrorReceiveCats = receiveErrorFavoriteCats
    val getErrorDeleteFavoriteCats = errorDeleteFavoriteCat

    fun deleteFavoriteCat(cat: FavoriteCatsResponse?) {
        val data = fetchKitties.value ?: return
        cat?.let { kitty ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    favoriteCatsModel.deleteFavoriteCat(cat.id).collect {
                        when (it) {
                            is NetworkResponse.Success -> {
                                data.filter { response ->
                                    response.id != kitty.id
                                }.let {
                                    randomKitties.postValue(it)
                                }
                            }
                            is NetworkResponse.NetworkError -> {
                                getErrorDeleteFavoriteCats.postValue(it.error)
                            }
                            is NetworkResponse.ApiError -> {
                                getErrorDeleteFavoriteCats.postValue(Throwable(SOME_API_ERROR))
                            }
                            is NetworkResponse.UnknownError -> {
                                it.error?.let { error ->
                                    getErrorDeleteFavoriteCats.postValue(error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val SOME_API_ERROR = "Sorry, we try to back in correct state own app"
    }
}