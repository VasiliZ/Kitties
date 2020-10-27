package com.github.rtyvZ.kitties.network.response

import com.google.gson.annotations.SerializedName

data class FavoriteCatsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("image")
    val catImage: CatImage
)

