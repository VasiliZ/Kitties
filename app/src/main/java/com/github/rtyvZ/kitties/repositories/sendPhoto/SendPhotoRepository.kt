package com.github.rtyvZ.kitties.repositories.sendPhoto

import android.net.Uri
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.common.models.Cat
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SendPhotoRepository {

    private val session = App.SessionStorage.getSession()

    suspend fun uploadPhoto(
        uri: Uri
    ) = flow {
        session?.let { userSession ->
            uri.path?.let { path ->
                val file = File(path)
                val uidAsRequestBody = session
                    .userId
                    .toRequestBody(
                        TEXT_MEDIA_TYPE
                            .toMediaType()
                    )

                emit(
                    Api.getApi().uploadImage(
                        App.ApiKeyProvider.getKey(),
                        MultipartBody.Part
                            .createFormData(
                                NAME_FOR_CREATE_FORM_DATA,
                                file.name,
                                file.asRequestBody(MEDIA_TYPE.toMediaType())
                            ), uidAsRequestBody
                    )
                )
            }
        }
    }

    fun saveCat(cat: Cat) {
        App.DataBaseProvider.getDataBase().getCatDao().insertCat(cat)
    }

    companion object {
        private const val NAME_FOR_CREATE_FORM_DATA = "file"
        private const val MEDIA_TYPE = "image/jpg"
        private const val TEXT_MEDIA_TYPE = "text/plain"
    }
}