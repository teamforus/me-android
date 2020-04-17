package io.forus.me.android.presentation.view.screens.main


import android.os.Bundle
import android.util.Log
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.activity.BaseActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability



/**
 * Main application screen. This is the app entry point.
 */
class MainActivity : BaseActivity() {

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    private val db = Injection.instance.databaseHelper
    private val settings = Injection.instance.settingsDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.forus.me.android.presentation.R.layout.activity_main)

        val isGooglePlayAvailable = checkPlayServices()
        Log.d("GOOGLE_PLAY_AVAILABLE", isGooglePlayAvailable.toString())

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
           // navigateToWelcomeScreen() //old behavior
            navigateToLogInsignUpScreen()
        }
    }

    override fun onStop() {
        super.onStop()
    }


    /**
     * Goes to the welcome screen.
     */
    private fun navigateToLogInsignUpScreen() {

        this.navigator.navigateToLoginSignUp(this)
        finish()
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

    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            }

            return false
        }

        return true
    }

}
