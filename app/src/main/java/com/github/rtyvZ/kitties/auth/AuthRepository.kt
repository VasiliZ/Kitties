package com.github.rtyvZ.kitties.auth

import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteUser: RemoteUser,
    private val sessionStorage: UserInternalStorageContract
) {

    @ExperimentalCoroutinesApi
    fun getUser(channel: Channel<String>) {
        if (getUserFromLocalStorage()) {
            GlobalScope.launch {
                sessionStorage.restoreSession()
                channel.send(sessionStorage.getSession()?.userId.toString())
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val user =
                    remoteUser.getUserUid()
                user?.let { firebaseUser ->
                    saveUser(UserSession(firebaseUser.uid))
                    channel.send(firebaseUser.uid)
                }
            }
        }
    }

    private fun getUserFromLocalStorage(): Boolean = sessionStorage.hasSession()

    private fun saveUser(userSession: UserSession) = sessionStorage.saveSession(userSession)

}

