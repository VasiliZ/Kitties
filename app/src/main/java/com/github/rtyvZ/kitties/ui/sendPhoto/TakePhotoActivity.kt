package com.github.rtyvZ.kitties.ui.sendPhoto

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.ui.main.ImageHelper
import com.github.rtyvZ.kitties.ui.services.SendCatService
import kotlinx.android.synthetic.main.photo_preview.*
import java.io.File

class TakePhotoActivity : AppCompatActivity(R.layout.photo_preview) {
    private var resultIntent = Intent()
    private val imageHelper = ImageHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendPhotoFab.setOnClickListener {
            startService()
            sendResult()
            finish()
        }

        takeFullPhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            imageHelper.setPick(photoACat)
        } else {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun takeFullPhoto() {
        resultIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager).let {
                val file: File? = try {
                    this.filesDir
                    imageHelper.createImageFile(this)
                } catch (e: Exception) {
                    null
                }
                file?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this, Strings.Const.AUTHORITY, it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, ACTIVITY_RESULT_CODE)
                }
            }
        }
    }

    private fun startService() {
        val sentPhotoIntent = Intent(this, SendCatService::class.java)
        sentPhotoIntent.data = imageHelper.getPhoto(this).path.toUri()
        startService(sentPhotoIntent)
    }

    private fun sendResult() {
        val resultIntent = Intent()
        resultIntent.putExtra(
            Strings.IntentConsts.EXTRA_SEND_UPLOAD,
            getString(R.string.data_sent)
        )
        setResult(ACTIVITY_RESULT_CODE, resultIntent)
    }


    companion object {
        const val ACTIVITY_RESULT_CODE = 1
    }
}