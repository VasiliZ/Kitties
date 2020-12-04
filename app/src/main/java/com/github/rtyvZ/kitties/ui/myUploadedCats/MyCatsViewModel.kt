package com.github.rtyvZ.kitties.ui.myUploadedCats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCatsViewModel : ViewModel() {

    private val myCatsRepository = MyCatsRepository()
    private val myCatsModel = MyCatsModel(myCatsRepository)
    private val errorWhileGetsMyCats = MutableLiveData<Throwable>()
    private val errorDeletingMyCats = MutableLiveData<Throwable>()
    private val mutableSuccessDeleteCat = MutableLiveData<Unit>()
    val errorWhileDeletingCat = errorDeletingMyCats
    val getErrorMyCats = errorWhileGetsMyCats
    val getSuccessDeleteCat = mutableSuccessDeleteCat
    fun deleteUploadedCat(cat: Cat, position: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                myCatsModel.deleteCat(cat.id).collect {
                    when (it) {
                        is NetworkResponse.Success -> {
                            App.DataBaseProvider.getDataBase().getCatDao().delete(cat)
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
                                App.DataBaseProvider.getDataBase().getCatDao().delete(cat)
                                mutableSuccessDeleteCat.postValue(Unit)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getKittiesFromDB() =
        myCatsRepository.getSavedCats()

}
