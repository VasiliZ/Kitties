package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.db.CatDatabase
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.repositories.myKitties.MyKittiesPagingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyCatsViewModel @Inject constructor(
    private val db: CatDatabase,
    private val repo: MyKittiesPagingRepo
) : ViewModel() {

    @Inject
    lateinit var myCatsModel: MyCatsModel
    private val errorWhileGetsMyCats = MutableLiveData<Throwable>()
    private val errorDeletingMyCats = MutableLiveData<Throwable>()
    private val mutableSuccessDeleteCat = MutableLiveData<Unit>()
    private val mutableKittiesFromDb = MutableLiveData<List<Cat>>()

    val errorWhileDeletingCat = errorDeletingMyCats
    val getErrorMyCats = errorWhileGetsMyCats
    val getSuccessDeleteCat = mutableSuccessDeleteCat
    val getKitties: LiveData<List<Cat>> = mutableKittiesFromDb

    val myKitties = repo.getKittiesFromDB().cachedIn(viewModelScope)

    fun deleteUploadedCat(cat: Cat) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                myCatsModel.deleteCat(cat.id).collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            db.getCatDao().delete(cat)
                        }
                        is NetworkResponse.NetworkError -> {
                            errorDeletingMyCats.postValue(it.error)
                        }
                        is NetworkResponse.ApiError -> {
                            Log.d("tag", it.body.toString())
                        }
                        is NetworkResponse.UnknownError -> {
                            it.error?.let { error ->
                                errorDeletingMyCats.postValue(error)
                            } ?: kotlin.run {
                                db.getCatDao().delete(cat)
                                mutableSuccessDeleteCat.postValue(Unit)
                            }
                        }
                    }
                }
            }
        }
    }
}

