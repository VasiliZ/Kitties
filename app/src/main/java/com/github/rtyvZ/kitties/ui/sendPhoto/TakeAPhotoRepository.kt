package com.github.rtyvZ.kitties.ui.sendPhoto

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.github.rtyvZ.kitties.ui.main.ImageHelper
import java.io.File
import javax.inject.Inject

class TakeAPhotoRepository @Inject constructor(
    private val imageHelper: ImageHelper,
    private val context: Context
) {

    fun getPhotoUri(file: File): Uri =
        FileProvider.getUriForFile(context, AUTHORITY, file)

    fun getImage() = imageHelper.getBitmap()

    fun createTempFile(): File =
        imageHelper.createImageFile(context)

    fun getPhotoUri() =
        imageHelper.getFileWithPhoto(context).path.toUri()


    companion object {
        const val AUTHORITY = "com.github.rtyvZ.kitties.fileprovider"
    }
}
