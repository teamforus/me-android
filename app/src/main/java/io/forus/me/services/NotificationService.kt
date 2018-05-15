package io.forus.me.services

import android.app.Notification
import android.content.Context
import android.os.Parcel
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import io.forus.me.R

/**
 * Created by martijn.doornik on 01/05/2018.
 */
class NotificationService {


    companion object {
        val CHANNEL_ID = "8545"
        fun push(context: Context, title: String, text:String) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
            val notification = builder.build()
            NotificationManagerCompat.from(context).notify(1, notification)
        }
    }
}