package com.github.rtyvZ.kitties.common.models

data class Cat(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    var choice: Int = -1
)