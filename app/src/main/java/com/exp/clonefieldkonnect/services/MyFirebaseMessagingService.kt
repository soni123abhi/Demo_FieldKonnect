package com.exp.clonefieldkonnect.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        println("remoteMessageremoteMessage=="+remoteMessage)
        val notification = remoteMessage.notification

        if (notification != null) {
//            val title = notification.title ?: "No Title"
//            val body = notification.body ?: "No Body"
//            val imageUrl = notification.imageUrl?.toString() ?: ""

            val mes: String = notification.body ?: "No Body"
            val title: String = notification.title ?: "No Title"
            val image: String =  notification.imageUrl?.toString() ?: ""

            println("Notification Title: $title")
            println("Notification Body: $mes")
            println("Notification Image URL: $image")

            if (image.isNotBlank()) {
                val bitmap = convertBitmap(image)
                notificationImage(title, mes, bitmap)
            } else {
                notification(title, mes)
            }
        } else {
            println("No notification payload found.")
        }


//        val mes: String = remoteMessage.getData().get("message")!!
//        val title: String = remoteMessage.getData().get("title")!!
//        val time: String = remoteMessage.getData().get("time")!!
//        val image: String = remoteMessage.getData().get("image")!!

        val count: Int = StaticSharedpreference.getInt("notificationCount", getApplicationContext())
        StaticSharedpreference.saveInt("notificationCount", count + 1, getApplicationContext())

       /* if (image == "") notification(title, mes) else {
            val bitmap: Bitmap? = convertBitmap(image)
            notificationImage(title, mes, bitmap)
        }*/
    }

    private fun convertBitmap(urlImage: String): Bitmap? {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL(urlImage)
            connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection!!.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        } finally {
            connection?.disconnect()
        }
    }

    fun notificationImage(title: String?, message: String?, bitmap: Bitmap?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

       var pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
       /* val uri = Uri.parse(
            ("android.resource://"
                    + getApplicationContext().getPackageName()) + "/" + R.raw.mix_positive
        )*/
        val builder = NotificationCompat.Builder(this, "lumax")
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
            )
            .setAutoCancel(true)
        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()
            val name = "name"
            val descriptionText = "channel"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("lumax", name, importance)
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.setSound(alarmSound, audioAttributes)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(106, builder.build())
    }

    fun notification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
      var  pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }


        /* val uri = Uri.parse(
            ("android.resource://"
                    + getApplicationContext().getPackageName()) + "/" + R.raw.mix_positive
        )*/
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
        println("tokentokentokentokentoken=="+s)
        StaticSharedpreference.saveInfoUnclear("fcmToken", s!!, getApplicationContext())
    }
}