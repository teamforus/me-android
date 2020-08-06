package io.forus.me.android.presentation.view.screens.account.restore_account_success


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.CommonActivity

/**
 * Created by maestrovs on 22.04.2020.
 */
class RestoreAccountSuccessActivity : CommonActivity() {


    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"
        private val IS_EXCHANGE_TOKEN = "IS_EXCHANGE_TOKEN"

        fun getCallingIntent(context: Context, token: String, isExchangeToken: Boolean): Intent {
            val intent = Intent(context, RestoreAccountSuccessActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            intent.putExtra(IS_EXCHANGE_TOKEN, isExchangeToken)
            return intent
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, RestoreAccountSuccessActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    private lateinit var fragment: RestoreAccountSuccessFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val token = intent.getStringExtra(TOKEN_EXTRA)
            val isExchangeToken = intent.getBooleanExtra(IS_EXCHANGE_TOKEN, true)
            fragment = if(token != null){
                RestoreAccountSuccessFragment.newIntent(token,isExchangeToken)
            } else RestoreAccountSuccessFragment()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        fragment.exchangeToken(intent.getStringExtra(TOKEN_EXTRA))
    }
}
