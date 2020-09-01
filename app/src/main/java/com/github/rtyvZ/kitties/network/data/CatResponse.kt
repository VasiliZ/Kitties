package com.github.rtyvZ.kitties.network.data

import com.github.rtyvZ.kitties.common.models.Cat
import com.google.gson.annotations.SerializedName


data class CatResponse(


    @SerializedName("id")
    val id: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int,
) {
    fun toCat(): Cat {
        return Cat(id, url, width, height)
    }
}

