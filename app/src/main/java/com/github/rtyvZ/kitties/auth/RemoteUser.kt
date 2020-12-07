package com.github.rtyvZ.kitties.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class RemoteUser @Inject constructor() {

    @Inject
    lateinit var auth: FirebaseAuth
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    @ExperimentalCoroutinesApi
    fun getUserUid() = getUserUIDFromFireBase(auth).result?.user


    private fun getUserUIDFromFireBase(auth: FirebaseAuth) =
        auth
            .signInAnonymously()
            .addOnCompleteListener(executor) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { nonNullUser ->
                        return@let nonNullUser
                    }
                }
            }
}