package com.github.rtyvZ.kitties.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

class RemoteUser @Inject constructor() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var executor: Executor

    @ExperimentalCoroutinesApi
    fun getUserUid(channel: Channel<String>) = getUserUIDFromFireBase(auth, channel)

    private fun getUserUIDFromFireBase(auth: FirebaseAuth, channel: Channel<String>) {
        auth
            .signInAnonymously()
            .addOnCompleteListener(executor) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { nonNullUser ->
                        GlobalScope.launch {
                            channel.send(nonNullUser.uid)
                        }
                    }
                }
            }
    }
}