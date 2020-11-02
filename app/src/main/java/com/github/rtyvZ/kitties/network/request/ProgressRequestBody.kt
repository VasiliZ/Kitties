package com.github.rtyvZ.kitties.network.request

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressRequestBody(
    private val file: File,
    val listener: (Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? {
        return "image/jpg".toMediaType()
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileSize = file.length()
        val byteArray = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded: Long = 0
        inputStream.use { fileInputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (fileInputStream.read().also { read = it } != -1) {
                handler.post(ProgressUpdater(fileSize, uploaded))
                uploaded += read
                sink.write(byteArray, 0, read)
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

    inner class ProgressUpdater(private val total: Long, private val upload: Long) : Runnable {
        override fun run() {
            val percent: Float = (upload.toFloat() / total) * 100
            val even = percent.toInt()
            listener.invoke(even)
        }
    }
}