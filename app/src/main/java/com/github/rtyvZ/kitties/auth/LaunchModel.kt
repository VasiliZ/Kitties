package com.github.rtyvZ.kitties.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject


class LaunchModel @Inject constructor(private val authRepo: AuthRepository) {

    @ExperimentalCoroutinesApi
    fun getUserUid(channel: Channel<String>) = authRepo.getUser(channel)
    fun saveUserUid(uid: String) {
        authRepo.saveUserUid(uid)
    }
}
