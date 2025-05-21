package com.example.medicall.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.medicall.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Nouveau token : $token")
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_MESSAGE", "Message reçu : ${remoteMessage.data}")

        val title = remoteMessage.notification?.title ?: "Nouveau message"
        val body = remoteMessage.notification?.body ?: "Tu as reçu une notification"
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, "Notif reçue : $title", Toast.LENGTH_LONG).show()
        }

        showNotification(title, body)
    }



    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel_id"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notifications Medicall", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
