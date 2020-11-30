package com.github.rtyvZ.kitties.ui.main

import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.rtyvZ.kitties.common.App

class MainActivityViewModel(app: App) : AndroidViewModel(app) {
    private val mutableImagePath = MutableLiveData<String>()
    val getRealPath: LiveData<String> = mutableImagePath

    fun getRealPathForImage(uri: Uri) {
        val cursor: Cursor?
        val columnIndexID: Int
        val projection = arrayOf(MainActivity.DATA)
        cursor = getApplication<App>().contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            it.moveToFirst()
            columnIndexID = cursor.getColumnIndexOrThrow(MainActivity.DATA)
            mutableImagePath.value = cursor.getString(columnIndexID)
        }
        cursor?.close()
    }
}