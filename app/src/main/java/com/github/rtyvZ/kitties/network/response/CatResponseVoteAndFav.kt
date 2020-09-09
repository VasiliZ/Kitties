package com.github.rtyvZ.kitties.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CatResponseVoteAndFav(
    @SerializedName("message")
    val message: String,
    @SerializedName("id")
    val id: Int
) : Parcelable