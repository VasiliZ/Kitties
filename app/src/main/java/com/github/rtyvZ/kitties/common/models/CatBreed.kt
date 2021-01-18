package com.github.rtyvZ.kitties.common.models

import com.google.gson.annotations.SerializedName

data class CatBreed(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("life_span")
    val lifeSpan: String,
    @SerializedName("weight_imperial")
    val weight_imperial: Int
)