package com.github.rtyvZ.kitties.ui.sendPhoto

import android.content.Context
import android.net.Uri
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SendPhotoRepository {

    private val session = App.SessionStorage.getSession()

    suspend fun uploadPhoto(
        file: File,
        listener: (Int) -> Unit,
        context: Context
    ) = flow {
        session?.let {
            val uri = Uri.fromFile(file)

            val requestBody =
                RequestBody.create(context.contentResolver.getType(uri)?.toMediaType(), file)
            val body =
                MultipartBody.Part
                    .createFormData("picture", file.name, requestBody)
            emit(
                Api.getApi().uploadImage(
                    App.ApiKeyProvider.getKey(),
                    body,
                    it.userId
                )
            )
        }
    }
}