package com.github.rtyvZ.kitties.common.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    @SerializedName("url")
    val url:String
):Parcelable