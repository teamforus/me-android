package io.forus.me.android.presentation.api_config

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.forus.me.android.presentation.view.screens.main.MainActivity

class Utils private constructor() {

    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = Utils()
    }

    companion object {
        val instance: Utils by lazy { Holder.INSTANCE }
    }


    fun restartApp(context: Context) {
        val mStartActivity = Intent(context, MainActivity::class.java)
        val mPendingIntentId = 123456
        val mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
        val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
        System.exit(0)
    }
}