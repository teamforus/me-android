package io.forus.me.android.presentation.view.screens.main


import android.os.Bundle
import io.forus.me.android.presentation.internal.Injection

import io.forus.me.android.presentation.view.activity.BaseActivity

/**
 * Main application screen. This is the app entry point.
 */
class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.forus.me.android.presentation.R.layout.activity_main)


       // Injection.instance.accountRepository.requestDelegatesQRAddress()


       // navigateToWelcomeScreen()
        navigateToDashboard()
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
