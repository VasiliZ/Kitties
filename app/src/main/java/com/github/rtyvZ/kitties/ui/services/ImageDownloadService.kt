package com.github.rtyvZ.kitties.ui.services

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import androidx.core.net.toUri
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.models.Cat
import java.io.File

class ImageDownloadService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val cat = it.getParcelableExtra<Cat>(Strings.IntentConsts.DOWNLOAD_IMAGE_KEY)
            cat?.let {
                val downloadManager =
                    applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val downloadUri = cat.url.toUri()
                val filename = cat.url.split("/").last()
                val request = DownloadManager.Request(downloadUri)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES,
                        File.separator + filename
                    )
                downloadManager.enqueue(request)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}