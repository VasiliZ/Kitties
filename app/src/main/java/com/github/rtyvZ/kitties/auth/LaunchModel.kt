package com.github.rtyvZ.kitties.auth

class LaunchModel {

    private val authRepo = AuthRepository()

    fun getUserUid() = authRepo.getUserUid()
}
