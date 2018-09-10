package io.forus.me.android.presentation.view.screens.main


import android.os.Bundle
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity

/**
 * Main application screen. This is the app entry point.
 */
class MainActivity : BaseActivity() {

    private val db = Injection.instance.databaseHelper
    private val settings = Injection.instance.settingsDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.forus.me.android.presentation.R.layout.activity_main)


        if(db.exists()){
            val locked = settings.isPinEnabled()
            if(locked){
                navigateToPinlock()
            }
            else {
                navigateToDashboard()
            }
        }
        else {
            navigateToWelcomeScreen()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    /**
     * Goes to the welcome screen.
     */
    private fun navigateToWelcomeScreen() {

        this.navigator.navigateToWelcomeScreen(this)
        finish()
    }

    /**
     * Goes to the dashboard screen.
     */
    private fun navigateToDashboard() {
        db.open("")
        this.navigator.navigateToDashboard(this)
        finish()
    }

    /**
     * Goes to the lock screen
     */
    private fun navigateToPinlock() {
        // Database will be opened later
        val useFingerprint = settings.isFingerprintEnabled()
        this.navigator.navigateToDashboardPinlocked(this, useFingerprint)
        finish()
    }

}
