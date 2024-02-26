package com.example.myapplication.notification


import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import java.util.*
import javax.inject.Inject


class MyNotificationManager @Inject constructor(private val app: Application) {



    fun textNotification(title: String?, message: String?, uri: String?) {
        val snoozeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        val snoozePendingIntent=
            PendingIntent.getActivity(app, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)
        val rand = Random()
        val idNotification = rand.nextInt(1000000000)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = app.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "Channel_id_default", "Channel_name_default", NotificationManager.IMPORTANCE_HIGH
            )
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChannel.description = "Channel_description_default"
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.setSound(soundUri, attributes)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder = NotificationCompat.Builder(app, "Channel_id_default")


        notificationBuilder.setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentTitle(title)
            .setContentText(message)
            .addAction(
                R.drawable.ic_launcher_foreground,
                app.getString(R.string.search), snoozePendingIntent
            )
        notificationManager.cancelAll()
        notificationManager.notify(idNotification, notificationBuilder.build())

    }


}