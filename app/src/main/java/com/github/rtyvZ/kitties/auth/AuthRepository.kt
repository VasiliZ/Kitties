package com.github.rtyvZ.kitties.auth

import com.github.rtyvZ.kitties.common.App
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AuthRepository {
    private lateinit var auth: FirebaseAuth
    private val checkConnectionProvider = App.ConnectionCheckerProvider
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    fun getUserUid() = flow {
        var fireBaseUser: String? = null
        if (checkConnectionProvider.checkConnection().isNetworkConnected()) {
            auth = FirebaseAuth.getInstance()
            auth.signInAnonymously().addOnCompleteListener(executor) {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { nonNullUser ->
                        fireBaseUser = nonNullUser.uid
                    }
                }
            }
        }
        emit(fireBaseUser)
    }
}