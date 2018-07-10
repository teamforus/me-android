package io.forus.me.android.presentation

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDex
import io.forus.me.android.data.entity.database.DaoMaster
import io.forus.me.android.data.entity.database.DaoSession
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.presentation.internal.Injection
import android.os.StrictMode.setThreadPolicy



//import com.squareup.leakcanary.LeakCanary

/**
 * Android Main Application
 */
class AndroidApplication : Application() {

    private var me: AndroidApplication? = null
    private var daoSession : DaoSession? = null

    override fun onCreate() {
        super.onCreate()
        this.initializeInjector()
        this.initializeLeakDetection()
        this.initDatabase()
        this.initRetrofit()

        this.me = this

    }

    private fun initRetrofit() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        MeServiceFactory.init(applicationContext, Injection.instance.accountLocalDataSource)
    }



    private fun initDatabase() {
        val helper = DaoMaster.DevOpenHelper(this, "main-db")
        val db =  helper.getEncryptedWritableDb(getString(R.string.database_secrete_key))
        daoSession = DaoMaster(db).newSession()
        Injection.instance.daoSession = daoSession
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
