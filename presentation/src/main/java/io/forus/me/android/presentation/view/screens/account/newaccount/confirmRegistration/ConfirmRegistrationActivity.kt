package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.CommonActivity


class ConfirmRegistrationActivity : CommonActivity() {


    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun getCallingIntent(context: Context, token: String): Intent {
            val intent = Intent(context, ConfirmRegistrationActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            return intent
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ConfirmRegistrationActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    private lateinit var fragment: ConfirmRegistrationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val token = intent.getStringExtra(TOKEN_EXTRA)
            fragment = if(token != null){
                ConfirmRegistrationFragment.newIntent(token)
            } else ConfirmRegistrationFragment()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        fragment.exchangeToken(intent.getStringExtra(TOKEN_EXTRA))
    }
}
