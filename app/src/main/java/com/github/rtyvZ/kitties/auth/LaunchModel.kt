package com.github.rtyvZ.kitties.auth

import kotlinx.coroutines.channels.Channel


class LaunchModel {

    private val authRepo = AuthRepository()

    fun getUserUid(channel: Channel<String>) = authRepo.getUserUid(channel)
}
