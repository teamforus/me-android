package io.forus.me.android.presentation.view.screens.account.assigndelegates.email


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.CommonActivity
import androidx.activity.viewModels
import io.forus.me.android.presentation.view.base.MViewModelProvider

/**
 * Main application screen. This is the app entry point.
 */
class RestoreByEmailActivity : CommonActivity(), MViewModelProvider<RestoreByEmailViewModel> {

    override val viewModel: RestoreByEmailViewModel by viewModels()

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
            val token = intent.getStringExtra(TOKEN_EXTRA)

            fragment = if(token != null){
                viewModel.setToken(token)
                RestoreByEmailFragment.newIntent(token)
            } else RestoreByEmailFragment()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.getStringExtra(TOKEN_EXTRA)?.let {
            fragment.exchangeToken(it)
        }

    }
}
