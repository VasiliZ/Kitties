package com.github.rtyvZ.kitties.ui.sendPhoto

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.network.MyResult
import com.github.rtyvZ.kitties.network.request.ProgressRequestBody
import com.github.rtyvZ.kitties.network.response.UploadCatResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SendPhotoModel {

    private val session = App.SessionStorage.getSession()

    suspend fun uploadPhoto(
        file: File,
        listener: (Int) -> Unit
    ): MyResult<UploadCatResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                session?.let {
                    val requestBody = ProgressRequestBody(
                        file, "multipart/form-data",
                        listener
                    )
                    file.asRequestBody("multipart/form-data".toMediaType())
                    val body =
                        MultipartBody.Part
                            .create(requestBody)
                    val answer = Api.getApi().uploadImage(
                        App.ApiKeyProvider.getKey(),
                        body
                    ).execute()
                        .body()
                    MyResult.Success(
                        answer
                    )
                } ?: kotlin.run { MyResult.Error(IllegalStateException("")) }
            } catch (e: Exception) {
                MyResult.Error(e)
            }
        }
    }
}