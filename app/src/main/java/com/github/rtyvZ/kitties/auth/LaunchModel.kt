package com.github.rtyvZ.kitties.auth

import com.github.rtyvZ.kitties.common.models.UserSession
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject


class LaunchModel @Inject constructor(private val authRepo: AuthRepository) {

    fun getUserUid(channel: Channel<String>) {
        authRepo.getUser(channel)
    }

    fun saveUserUid(uid: String) {
        authRepo.saveUserUid(uid)
    }

    fun getKeyFromFirestore(channelFromKey: Channel<String>) {
        authRepo.getApiKey(channelFromKey)
    }

    fun encryptKey(key: String) {
        authRepo.encryptKey(key)
    }
}
