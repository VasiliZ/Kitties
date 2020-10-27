package com.github.rtyvZ.kitties.network.response

import com.google.gson.annotations.SerializedName

data class CatImage(

    @SerializedName("url")
    val url: String
)