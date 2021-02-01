package com.github.rtyvZ.kitties.ui.main

import android.content.Context
import android.graphics.*
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ImageHelper @Inject constructor() {


    private lateinit var currentPhotoPath: String

    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File = context.filesDir
        return File.createTempFile(
            "JPEG_${timeStamp}",
            SUFFIX_FILE,
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun getBitmap(): Bitmap? {

        var resultBitmap: Bitmap?
        val bitmapOptions = BitmapFactory.Options()

        BitmapFactory
            .decodeFile(currentPhotoPath, bitmapOptions).also {
                it.let {
                    resultBitmap = rotateBitmap(it)
                }
            }
        return resultBitmap
    }

    private fun rotateBitmap(bitmap: Bitmap): Bitmap? {
        val exifInterface = ExifInterface(currentPhotoPath)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val rotatedBitmap: Bitmap?

        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(bitmap, 90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(bitmap, 180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(bitmap, 270)
            }
            else -> {
                bitmap
            }
        }
        return rotatedBitmap
    }

    fun getFileWithPhoto(context: Context): File {
        return File(context.filesDir, File(currentPhotoPath).name)
    }

    companion object {
        const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        const val SUFFIX_FILE = ".jpg"
    }
}