package com.github.rtyvZ.kitties.ui.sendPhoto

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.network.request.ProgressRequestBody
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SendPhotoModel {

    private val session = App.SessionStorage.getSession()

    suspend fun uploadPhoto(
        file: File,
        listener: (Int) -> Unit
    ) = flow {
        session?.let {
            val requestBody = ProgressRequestBody(
                file, "multipart/form-data",
                listener
            )
            file.asRequestBody("multipart/form-data".toMediaType())
            val body =
                MultipartBody.Part
                    .create(requestBody)
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