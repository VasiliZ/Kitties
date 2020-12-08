package com.github.rtyvZ.kitties.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.Executor
import javax.inject.Inject

class RemoteUser @Inject constructor() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var executor: Executor

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