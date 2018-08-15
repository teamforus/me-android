package io.forus.me.android.presentation.view.screens.account.newaccount


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.BaseActivity

/**
 * Main application screen. This is the app entry point.
 */
class NewAccountActivity : BaseActivity() {


    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, NewAccountActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, NewAccountFragment())
        }
    }
}
