package com.github.rtyvZ.kitties.ui.sendPhoto

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.ui.main.ImageHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.photo_preview.*
import java.io.File

class TakePhotoActivity : AppCompatActivity(R.layout.photo_preview) {
    private var resultIntent = Intent()

    private val viewModel: SendPhotoViewModel by viewModels()
    private val listener: (Int) -> Unit = {
        sendPhotoFab.isClickable = false
        backToCatsFAB.visibility = View.GONE
        stateUpload.text = getString(R.string.uploading)

        photoACat.setProgress(it)

        when (it) {
            100 -> {
                sendPhotoFab.visibility = View.GONE
                backToCatsFAB.visibility = View.VISIBLE
                stateUpload.text = getString(R.string.uploaded)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendPhotoFab.setOnClickListener {
            viewModel.sendPhoto(ImageHelper.getPhoto(this), listener)
        }

        takeFullPhoto()

        backToCatsFAB.setOnClickListener {

        }

        viewModel.getStateSendPhoto.observe(this, {

            Snackbar.make(photoACat, it.toString(), Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            ImageHelper.setPick(photoACat)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun takeFullPhoto() {
        resultIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager).let {
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
            }
        }
    }

    companion object {
        const val ACTIVITY_RESULT_CODE = 1
        const val answer = "answer"
    }
}