package net.luan.cym.util

import net.luan.cym.*
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// notification IDs
private val NOTIFICATION_ID = 420
private val REQUEST_CODE = 69

fun NotificationManager.send(message: String, applicationContext: Context) {
    // make a pending intent that sends you to MainActivity
    val intent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    // create the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id))
        .setSmallIcon(R.drawable.ic_splash)
        .setContentTitle(applicationContext.getString(R.string.notification_channel_name))
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancel() {
    cancelAll()
}