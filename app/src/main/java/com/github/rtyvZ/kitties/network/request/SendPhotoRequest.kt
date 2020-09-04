package com.github.rtyvZ.kitties.network.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class SendPhotoRequest(
    /* @SerializedName("sub_id")
     val subId: String*/
    @SerializedName("file")
    val photo: MultipartBody.Part
)