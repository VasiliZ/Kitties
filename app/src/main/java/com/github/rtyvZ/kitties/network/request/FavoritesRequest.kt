package com.github.rtyvZ.kitties.network.request

import com.google.gson.annotations.SerializedName

data class FavoritesRequest(

    @SerializedName("image_id")
    val imageId: String,

    @SerializedName("sub_id")
    val userId: String
)