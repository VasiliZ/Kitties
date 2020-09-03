package com.github.rtyvZ.kitties.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.github.rtyvZ.kitties.R
import kotlinx.android.synthetic.main.photo_preview.*
import java.io.File

class TakePhotoActivity : AppCompatActivity(R.layout.photo_preview) {
    private var resultIntent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendPhotoFab.setOnClickListener {
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        takeFullPhoto()

        backToCatsFAB.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            ImageHelper.setPick(photoACat)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun takeFullPhoto() {
        resultIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.let {
                val file: File? = try {
                    this.filesDir
                    ImageHelper.createImageFile(this)
                } catch (e: Exception) {
                    null
                }
                file?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this, "com.github.rtyvZ.kitties.fileprovider", it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, ACTIVITY_RESULT_CODE)
                }
            } ?: kotlin.run {
                Log.d("error", "err")
            }
        }
    }

    companion object {
        const val ACTIVITY_RESULT_CODE = 1
        const val PHOTO_THUMBNAIL = "data"
    }
}