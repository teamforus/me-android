package io.forus.me.android.presentation.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.forus.me.android.presentation.internal.Injection

/**
 * Created by pavelpantuhov on 16.02.2018.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        /*val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT)

        remoteMessage?.notification?.let { message ->
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(getString(R.string.default_notification_channel_id),
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.ic_stat_account_circle)
                    .setContentTitle(message.title)
                    .setContentText(message.body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            notificationManager.notify(0, notificationBuilder.build())
        }*/

    }

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        Log.d("NEW_FCM_TOKEN", s)
        s?.let { Injection.instance.accountRepository.registerFCMToken(it) }
    }

    companion object {
        private val TAG = "FCM Service"
    }
}