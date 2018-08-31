package io.forus.me.android.presentation.view.screens.account.account


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.activity.CommonActivity

/**
 * Main application screen. This is the app entry point.
 */
class AccountActivity : CommonActivity() {


    companion object {


        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AccountActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, AccountFragment())
        }
    }
}
