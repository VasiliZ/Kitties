package com.github.rtyvZ.kitties.network.response

import com.google.gson.annotations.SerializedName

data class MyVoteResponse(
    @SerializedName("value")
    val voteValue: Int,
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("id")
    val idVote: String
)
