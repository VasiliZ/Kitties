package com.github.rtyvZ.kitties.network.response

import com.google.gson.annotations.SerializedName

class ApiErrorResponse(
    @SerializedName("message")
    val message: String
)