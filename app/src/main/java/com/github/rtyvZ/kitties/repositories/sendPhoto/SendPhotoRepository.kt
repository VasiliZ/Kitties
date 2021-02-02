package com.github.rtyvZ.kitties.repositories.sendPhoto

import android.net.Uri
import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.db.CatDatabase
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class SendPhotoRepository @Inject constructor(
    private val sessionStorage: UserInternalStorageContract,
    private val api: Api,
    private val db: CatDatabase
) {

    private val session = sessionStorage.getSession()

    suspend fun uploadPhoto(
        uri: Uri
    ) = flow {
        session?.let { _ ->
            uri.path?.let { path ->
                val file = File(path)
                val uidAsRequestBody = session
                    .userId
                    .toRequestBody(
                        TEXT_MEDIA_TYPE
                            .toMediaType()
                    )

                emit(
                    api.uploadImage(
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
        db.getCatDao().insertCat(cat)
    }

    companion object {
        private const val NAME_FOR_CREATE_FORM_DATA = "file"
        private const val MEDIA_TYPE = "image/jpg"
        private const val TEXT_MEDIA_TYPE = "text/plain"
    }
}