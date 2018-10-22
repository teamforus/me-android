package io.forus.me.android.presentation

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDex
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.presentation.internal.Injection
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import android.app.Activity
import android.os.Bundle
import android.content.pm.ActivityInfo
import com.google.android.gms.common.GoogleApiAvailability


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

    private fun initFabric() {
        Fabric.with(this, Crashlytics())
    }
    private fun initRetrofit() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        MeServiceFactory.init(applicationContext, Injection.instance.accountLocalDataSource)
    }

    private fun initDb(){
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

    private fun initPortraitOrientation(){
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

    private fun initFirebase(){

    }

}
