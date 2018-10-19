package io.forus.me.android.presentation.view.screens.account.assigndelegates.email


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.CommonActivity

/**
 * Main application screen. This is the app entry point.
 */
class RestoreByEmailActivity : CommonActivity() {


    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun getCallingIntent(context: Context, token: String): Intent {
            val intent = Intent(context, RestoreByEmailActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            return intent
        }

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, RestoreByEmailActivity::class.java)
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    private lateinit var fragment: RestoreByEmailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if(intent.hasExtra(TOKEN_EXTRA)){
                fragment = RestoreByEmailFragment.newIntent(intent.getStringExtra(TOKEN_EXTRA))
            }
            else fragment = RestoreByEmailFragment()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        fragment.exchangeToken(intent.getStringExtra(TOKEN_EXTRA))
    }
}
