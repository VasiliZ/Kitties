package com.github.rtyvZ.kitties.auth

import kotlinx.coroutines.channels.Channel
import javax.inject.Inject


class LaunchModel @Inject constructor(val authRepo: AuthRepository) {

    fun getUserUid(channel: Channel<String>) = authRepo.getUserUid(channel)
}
