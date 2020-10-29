package com.github.rtyvZ.kitties.ui.sendPhoto

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.network.request.ProgressRequestBody
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
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
                            ProgressRequestBody(file, listener)
                        )
                )
            )
        }
    }

    companion object {
        private const val nameForCreateFormData = "file"
    }
}