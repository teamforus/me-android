package io.forus.me.android.presentation.view.screens.welcome


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.screens.lock.PinLockActivity

/**
 * Main application screen. This is the app entry point.
 */
class WelcomeActivity : BaseActivity() {


    companion object {
        private val ERROR_MESSAGE_EXTRA = "ERROR_MESSAGE_EXTRA"

        fun getCallingIntent(context: Context, error_message: String? = null): Intent {
            val intent =  Intent(context, WelcomeActivity::class.java)
            intent.putExtra(WelcomeActivity.ERROR_MESSAGE_EXTRA, error_message)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, WelcomeFragment.newIntent(intent.getStringExtra(WelcomeActivity.ERROR_MESSAGE_EXTRA)))
        }

    }

}
