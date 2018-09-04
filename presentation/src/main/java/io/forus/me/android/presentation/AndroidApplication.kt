package io.forus.me.android.presentation

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDex
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.presentation.internal.Injection
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric




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
        this.initFabric()

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

    private fun initializeInjector() {
        Injection.instance.applicationContext = applicationContext
//        this.applicationComponent = DaggerApplicationComponent.builder()
//                .applicationModule(ApplicationModule(this))
//                .build()
    }

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
           // LeakCanary.install(this)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
