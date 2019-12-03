package net.luan.cym.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log
import net.luan.cym.MainActivity
import net.luan.cym.R
import java.text.DateFormat
import java.util.*

class AlarmNotificationReceiver : BroadcastReceiver() {

    companion object {
        // Notification ID to allow for future updates
        private const val MY_NOTIFICATION_ID = 1
        private const val TAG = "AlarmNotificationReceiver"
        private const val CHANNEL_ID_STRING = ".channel_01"
    }

    private lateinit var mNotificationManager: NotificationManager

    // Notification Text Elements
    private val tickerText = "Call Your Mother notification"
    private val contentTitle = "A Kind Reminder"
    private val contentText = "You need to call someone!!"

    // Notification Sound and Vibration on Arrival
    private val mVibratePattern = longArrayOf(0, 200, 200, 300)
    private lateinit var mContext: Context
    private lateinit var mChannelID: String

    override fun onReceive(context: Context, intent: Intent) {

        mContext = context

        // Get the NotificationManager
        mNotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // if (null == mNotificationManager) return

        createNotificationChannel()

        // The Intent to be used when the user clicks on the Notification View
        val mNotificationIntent = Intent(context, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // The PendingIntent that wraps the underlying Intent
        val mContentIntent = PendingIntent.getActivity(
            context, 0,
            mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the Notification
        val notificationBuilder = Notification.Builder(
            mContext, mChannelID
        ).setTicker(tickerText)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true).setContentTitle(contentTitle)
            .setContentText(contentText).setContentIntent(mContentIntent)

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(
            MY_NOTIFICATION_ID,
            notificationBuilder.build()
        )

        // Log occurrence of notify() call
        Log.i(TAG,mContext.getString(R.string.sending_not_string) +
                DateFormat.getDateTimeInstance().format(Date()))
    }

    private fun createNotificationChannel() {

        mChannelID = mContext.packageName + CHANNEL_ID_STRING

        // The user-visible name of the channel.
        val name = mContext.getString(R.string.channel_name)

        // The user-visible description of the channel
        val description = mContext.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelID, name, importance)

        // Configure the notification channel.
        mChannel.description = description
        mChannel.enableLights(true)

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = mVibratePattern

        Log.i(TAG, "android.resource://" + mContext.packageName + "/"
                + R.raw.alarm_rooster)
        val soundUri = Uri
            .parse(
                "android.resource://" + mContext.packageName + "/"
                        + R.raw.alarm_rooster
            )
        mChannel.setSound(
            soundUri, AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        )

        mNotificationManager.createNotificationChannel(mChannel)
    }
}