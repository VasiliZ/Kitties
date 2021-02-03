package com.github.rtyvZ.kitties.auth

import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.cryptography.Cryptographer
import com.github.rtyvZ.kitties.common.models.UserSession
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteUser: RemoteUser,
    private val sessionStorage: UserInternalStorageContract,
) {

    @ExperimentalCoroutinesApi
    fun getUser(channel: Channel<String>) {
        sessionStorage.getSession()?.let {
            GlobalScope.launch {
                sessionStorage.restoreSession()
                channel.send(it.userId)
            }
        } ?: run {
            GlobalScope.launch(Dispatchers.IO) {
                remoteUser.getUserUid(channel)
                getApiKey(channel)
            }
        }
    }

    private fun saveUser(userSession: UserSession) = sessionStorage.saveSession(userSession)
    fun saveUserUid(uid: String) {
        saveUser(UserSession(uid))
    }

    fun getApiKey(channelFromKey: Channel<String>) {
        val remoteDB = Firebase.firestore
        val documentReference = remoteDB.collection(COLLECTION_KEY)
            .document(DOCUMENT_KEY)
        documentReference.get().addOnSuccessListener { document ->
            if (document.exists()) {
                GlobalScope.launch {
                    channelFromKey.send(document.data?.values.toString())
                    encryptKey(
                        document.data?.values.toString()
                    )
                }
            }
        }
    }

    fun encryptKey(key: String) {
        val encryptor: Cryptographer<String, String> = Cryptographer()
        val encr = encryptor.crypt(key.substring(1, key.length - 1))
        encr?.let {
            sessionStorage.saveEncryptedToken(it.first)
            sessionStorage.saveSecretKey(it.second)
        }
    }

    companion object {
        const val DOCUMENT_KEY = "header"
        const val COLLECTION_KEY = "key"
    }
}

