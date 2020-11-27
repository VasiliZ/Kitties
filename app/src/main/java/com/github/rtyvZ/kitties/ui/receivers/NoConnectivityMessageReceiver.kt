package com.github.rtyvZ.kitties.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.github.rtyvZ.kitties.common.Strings

class NoConnectivityMessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val message = it.getStringExtra(Strings.IntentConsts.SEND_NO_CONNECTIVITY_KEY)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}