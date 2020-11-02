package com.github.rtyvZ.kitties.ui.sendPhoto

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
        listener: (Int) -> Unit
    ) = flow {
        session?.let {
            emit(
                Api.getApi().uploadImage(
                    App.ApiKeyProvider.getKey(),
                    MultipartBody.Part
                        .createFormData(
                            nameForCreateFormData,
                            file.name,
                            RequestBody.create("image/jpg".toMediaType(), file)
                        )
                )
            )
        }
    }

    companion object {
        private const val nameForCreateFormData = "file"
    }
}