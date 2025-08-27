package com.exp.clonefieldkonnect.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            val title = data["title"] ?: "No Title"
            val message = data["body"] ?: "No Body"
            val imageUrl = data["image"] ?: ""

            println("Title: $title")
            println("Message: $message")
            println("Image URL: $imageUrl")

            StaticSharedpreference.saveInfo(Constant.NOTIFICATION_TYPE, imageUrl, this)
            notification(title, message, imageUrl)
        }
    }


    fun notification(title: String?, message: String?, image22: String) {

        println("image22image22image22=="+image22)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(this, "lumax")
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setAutoCancel(true)
        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val name = "name"
            val descriptionText = "channel"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("lumax", name, importance)
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.setSound(alarmSound,audioAttributes)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(106, builder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
//        dMAH4gW4R_-I5Aqe5ZQkre:APA91bHHNpXI9sb2DfPfCG_ThS-uA7Zm1vi9Uj3tEuqH8ItWQoeqq7l-SmPGFDNqsTZGuyRUOj52M6wbbEkZQMgHtZJ_ywQY_nGEwFH1VVokTKkY6qPN9DY
        println("tokentokentokentokentoken=="+s)
        StaticSharedpreference.saveInfoUnclear("fcmToken", s!!, getApplicationContext())
    }
}