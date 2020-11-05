package com.github.rtyvZ.kitties.ui.sendPhoto

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.repositories.sendPhoto.SendPhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SendCatService : Service() {
    private val repo = SendPhotoRepository()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.data?.let { uri ->
            createNotificationChannel()
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    execUpload(uri)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun buildNotification(title: String, text: String) {
        val builder = NotificationCompat.Builder(this, Strings.Notification.CHANNEL_ID)
            .setSmallIcon(R.drawable.kitty)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        makeNotification(builder)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Strings.Notification.CHANNEL_NAME
            val descriptionText = Strings.Notification.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(Strings.Notification.CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun makeNotification(builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(this)) {
            notify(KITTIES_UPLOAD_NOTIFY_ID, builder.build())
        }
    }

    companion object {
        const val KITTIES_UPLOAD_NOTIFY_ID = 1101
    }

    private suspend fun execUpload(uri: Uri) {
        repo.uploadPhoto(uri)
            .catch { e ->
                if (e is HttpException) {
                    buildNotification(
                        getString(R.string.error),
                        getString(R.string.wrong_photo)
                    )
                } else {
                    buildNotification(
                        getString(R.string.error),
                        getString(R.string.some_kind_of_exception)
                    )
                }
            }.collect {
                buildNotification(
                    getString(R.string.success),
                    getString(R.string.success_upload_photo)
                )
            }
    }
}
