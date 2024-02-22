package com.example.myapplication.notification

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationService : FirebaseMessagingService() {
    private val TAG = "MainActivity"
    override fun onCreate() {
        super.onCreate()
        mNotificationManager= MyNotificationManager(application)
    }


    private lateinit var mNotificationManager: MyNotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
     remoteMessage.notification
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data Payload: " + remoteMessage.data)
            try {
                val title = remoteMessage.data["title"]
                val message = remoteMessage.data["message"]
                val uri=remoteMessage.data["uri"]

                mNotificationManager.textNotification(title, message,uri)

            } catch (e: Exception) {
                Log.d(TAG, "Exception: " + e.message)
            }
        }

    }
}