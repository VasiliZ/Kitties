package com.github.rtyvZ.kitties.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class VoteRequest(
    @SerializedName("image_id")
    val id: String,
    @SerializedName("value")
    val value: Int,
    @SerializedName("sub_id")
    val userId: String
) : Parcelable