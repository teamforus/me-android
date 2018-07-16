package io.forus.me.android.presentation.view.screens.main


import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import io.forus.me.android.presentation.internal.Injection

import io.forus.me.android.presentation.view.activity.BaseActivity

/**
 * Main application screen. This is the app entry point.
 */
class MainActivity : BaseActivity() {

    private  val  keyguardManager: KeyguardManager
        get() {
            return this.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        }

    private var deviceSecurityAlert: AlertDialog? = null


    fun isDeviceSecure(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) keyguardManager.isDeviceSecure else keyguardManager.isKeyguardSecure
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.forus.me.android.presentation.R.layout.activity_main)


        if (Injection.instance.accountLocalDataSource.isLogin()) {
            if (!systemServices.isDeviceSecure()) {
                deviceSecurityAlert = systemServices.showDeviceSecurityAlert()
            } else {
                navigateToDashboard()
            }

            //navigateToDashboard()
        } else {
            navigateToWelcomeScreen()
        }
    }

    override fun onStop() {
        super.onStop()
        deviceSecurityAlert?.dismiss()
    }

    /**
     * Goes to the welcome screen.
     */
    private fun navigateToWelcomeScreen() {

        this.navigator.navigateToWelcomeScreen(this)
        finish()
    }
    /**
     * Goes to the welcome screen.
     */
    private fun navigateToDashboard() {

        this.navigator.navigateToDashboard(this)
        finish()
    }

}
