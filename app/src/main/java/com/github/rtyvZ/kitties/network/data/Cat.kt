package com.github.rtyvZ.kitties.network.data

import com.google.gson.annotations.SerializedName

data class Cat(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    var isSetLike: Boolean = false
)