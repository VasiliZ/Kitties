package com.github.rtyvZ.kitties.common.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CatBreed(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("life_span")
    val lifeSpan: String,
    @SerializedName("weight_imperial")
    val weight_imperial: Int,
    @SerializedName("image")
    val image: Image,
    @SerializedName("description")
    val description: String
) : Parcelable