package io.forus.me.android.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.StrictMode
import android.support.multidex.MultiDex
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.fabric.sdk.android.Fabric
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.presentation.internal.Injection


//import com.squareup.leakcanary.LeakCanary

/**
 * Android Main Application
 */
class AndroidApplication : Application() {

    private var me: AndroidApplication? = null

    override fun onCreate() {
        super.onCreate()
        this.initializeInjector()
        this.initializeLeakDetection()
        this.initRetrofit()
        this.initDb()

        if (!BuildConfig.DEBUG)
            this.initFabric()

        this.initPortraitOrientation()
        this.initFirebase()

        this.me = this

    }

    private fun sendId(token: String) {
        Injection.instance.accountRepository.registerFCMToken(token)
    }

    private fun initFabric() {
        Fabric.with(this, Crashlytics())
    }

    private fun initRetrofit() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        MeServiceFactory.init(applicationContext, Injection.instance.accountLocalDataSource)
    }

    private fun initDb() {
        Injection.instance.databaseHelper
        Injection.instance.settingsDataSource
    }

    private fun initializeInjector() {
        Injection.instance.applicationContext = applicationContext
    }

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            // LeakCanary.install(this)
        }
    }

    private fun initPortraitOrientation() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initFirebase() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    Log.d("PUSH", token ?: "null")
                    token?.let { sendId(it) }
                })
    }

}
