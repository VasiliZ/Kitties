package com.github.rtyvZ.kitties.common.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Cat(
    @PrimaryKey(autoGenerate = true)
    val tableId: Long = 0L,
    @ColumnInfo(name = "id_picture")
    @SerializedName("id")
    val id: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "width")
    @SerializedName("width")
    val width: Int,
    @ColumnInfo(name = "height")
    @SerializedName("height")
    val height: Int,
    var choice: Int = -1,
    var voteId: Int = -1
)