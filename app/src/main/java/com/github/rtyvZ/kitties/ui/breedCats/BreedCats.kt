package com.github.rtyvZ.kitties.ui.breedCats

import com.google.gson.annotations.SerializedName

class BreedCats(

    @SerializedName("id")
    val idBreed: String,
    @SerializedName("name")
    val nameBreed: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("reference_image_id")
    val imageId: String?,
    var url: String = ""

)
