package com.github.rtyvZ.kitties.common.models

data class UserSession(
    var userId: String,
    var secretKey: String = "",
    var encryptedKey: String = ""
)