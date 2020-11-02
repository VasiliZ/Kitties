package com.github.rtyvZ.kitties.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AuthRepository {
    private lateinit var auth: FirebaseAuth
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    @ExperimentalCoroutinesApi
    fun getUserUid(chanel: Channel<String>) = runBlocking(Dispatchers.IO) {
        auth = FirebaseAuth.getInstance()
        launch {
            getUserUID(auth, chanel)
        }
    }

    private suspend fun getUserUID(auth: FirebaseAuth, chanel: Channel<String>) {
        auth
            .signInAnonymously()
            .addOnCompleteListener(executor) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    user?.let { nonNullUser ->
                        runBlocking {
                            chanel.send(nonNullUser.uid)
                        }
                    }
                }
            }
    }
}