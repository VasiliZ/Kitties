package com.github.rtyvZ.kitties.ui.main

import android.content.Context
import android.database.Cursor
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainRepository @Inject constructor(@ApplicationContext private val context: Context) {
    fun getPath(uri: Uri): String {
        var path = ""
        val cursor: Cursor?
        val columnIndexID: Int
        val projection = arrayOf(MainActivity.DATA)
        cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            it.moveToFirst()
            columnIndexID = cursor.getColumnIndexOrThrow(MainActivity.DATA)
            path = cursor.getString(columnIndexID)
        }
        cursor?.close()
        return path
    }
}