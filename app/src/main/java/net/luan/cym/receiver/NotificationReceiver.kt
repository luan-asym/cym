package net.luan.cym.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import net.luan.cym.*
import net.luan.cym.util.*

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java) as
                    NotificationManager

        notificationManager.send(intent.getStringExtra("message"), context)
    }
}