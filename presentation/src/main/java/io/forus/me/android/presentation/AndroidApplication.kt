package io.forus.me.android.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.StrictMode
import androidx.multidex.MultiDex
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import io.fabric.sdk.android.Fabric
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.presentation.api_config.ApiConfig
import io.forus.me.android.presentation.internal.Injection

/**
 * Android Main Application
 */
class AndroidApplication : Application() {

    private var me: AndroidApplication? = null

    override fun onCreate() {
        super.onCreate()

        updateAndroidSecurityProvider()

        ApiConfig.SERVER_URL = BuildConfig.SERVER_URL

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

    /**
     * This must fix SSLException for old devices
     *
     * @see {https://stackoverflow.com/a/42471738/3212712}
     */
    private fun updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            println("Google Play Services not available.")
        } catch (e: GooglePlayServicesNotAvailableException) {
            println("Google Play Services not available.")
        }
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
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }



            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initFirebase() {

        FirebaseFirestore.getInstance().apply {

            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        }
        FirebaseFirestore.getInstance().addSnapshotsInSyncListener {
            Log.d("FirebaseFirestore","FirebaseFirestore addSnapshotsInSyncListener")
        }

        FirebaseStorage.getInstance().apply {
            maxOperationRetryTimeMillis = 2000
            maxDownloadRetryTimeMillis = 2000
        }


    }

}
