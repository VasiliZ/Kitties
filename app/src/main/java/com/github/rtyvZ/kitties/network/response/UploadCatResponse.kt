package com.github.rtyvZ.kitties.network.response

import com.google.gson.annotations.SerializedName

data class UploadCatResponse(
    @SerializedName("message")
    val message: String
)