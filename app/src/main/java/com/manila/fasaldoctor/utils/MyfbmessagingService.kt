package com.manila.fasaldoctor.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.ChatOpenActivity
import com.manila.fasaldoctor.activity.HomeActivity
import java.util.Random

class MyfbmessagingService : FirebaseMessagingService() {

    private val channelID = "Krishi MITr"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {

        // if not works change to home

        val intent  = Intent(this,HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)

        createNotificationChannel(manager as NotificationManager)

        val intento = PendingIntent.getActivities(this,0,
            arrayOf(intent),PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,channelID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["messege"])
            .setSmallIcon(R.drawable.rishilogo)
            .setContentIntent(intento).build()

        manager.notify(kotlin.random.Random.nextInt(),notification)


        super.onMessageReceived(message)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager){

        val channel = NotificationChannel(channelID,"KrishiMITr",
            NotificationManager.IMPORTANCE_HIGH)
        channel.description = "New Chat"
        channel.enableLights(true)


        manager.createNotificationChannel(channel)


    }


}