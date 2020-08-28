package com.github.rtyvZ.kitties.auth

import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.common.executor
import com.github.rtyvZ.kitties.network.MyResult
import com.github.rtyvZ.kitties.network.NoConnectivityException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchModel {
    private lateinit var auth: FirebaseAuth
    private val checkConnectionProvider = App.ConnectionCheckerProvider
    private val resourcesProvider = App.ResourceProvider

    suspend fun getUserUid(resultCallback: (MyResult<String>) -> Unit) {
        return withContext(Dispatchers.IO) {
            if (checkConnectionProvider.checkConnection().isNetworkConnected()) {
                auth = FirebaseAuth.getInstance()
                auth.signInAnonymously().addOnCompleteListener(executor) {
                    if (it.isSuccessful) {
                        val user = auth.currentUser
                        user?.let { nonNullUser ->
                            resultCallback(MyResult.Success(nonNullUser.uid))
                        }
                    } else {
                        resultCallback(
                            MyResult.Error(
                                IllegalStateException(
                                    resourcesProvider.getString(
                                        R.string.whats_happens
                                    )
                                )
                            )
                        )
                    }
                }
            } else {
                resultCallback(MyResult.Error(NoConnectivityException()))
            }
        }
    }
}
