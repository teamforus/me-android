package io.forus.me.android.presentation.view.screens.account.send_crash_reports


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.CommonActivity

/**
 * Created by maestrovs on 23.04.2020.
 */
class SendReportsActivity : CommonActivity() {


    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun getCallingIntent(context: Context, token: String): Intent {
            val intent = Intent(context, SendReportsActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            return intent
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SendReportsActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    private lateinit var fragment: SendReportsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val token = intent.getStringExtra(TOKEN_EXTRA)
            fragment = if(token != null){
                SendReportsFragment.newIntent(token)
            } else SendReportsFragment()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    /*override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        fragment.exchangeToken(intent.getStringExtra(TOKEN_EXTRA))
    }*/
}
