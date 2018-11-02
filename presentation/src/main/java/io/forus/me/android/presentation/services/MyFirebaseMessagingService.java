package io.forus.me.android.presentation.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.forus.me.android.presentation.helpers.AppSettings;
import io.forus.me.android.presentation.internal.Injection;

/**
 * Created by pavelpantuhov on 16.02.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "FCM From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_FCM_TOKEN", s);
        new AppSettings(getApplicationContext()).updateFCMToken();
    }
}