package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.db.CatDatabase
import com.github.rtyvZ.kitties.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyCatsViewModel @Inject constructor(private val db: CatDatabase) : ViewModel() {
    @Inject
    lateinit var myCatsModel: MyCatsModel
    private val errorWhileGetsMyCats = MutableLiveData<Throwable>()
    private val errorDeletingMyCats = MutableLiveData<Throwable>()
    private val mutableSuccessDeleteCat = MutableLiveData<Unit>()
    val errorWhileDeletingCat = errorDeletingMyCats
    val getErrorMyCats = errorWhileGetsMyCats
    val getSuccessDeleteCat = mutableSuccessDeleteCat
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

    fun getKittiesFromDB() =
        myCatsModel.getLocalCats()

}
