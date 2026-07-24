package io.forus.me.android.presentation.view.screens.account.restore_account_exchange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider

class RestoreAccountExchangeActivity : CommonActivity(), MViewModelProvider<RestoreAccountExchangeViewModel> {

    override val viewModel: RestoreAccountExchangeViewModel by viewModels()

    companion object {
        private val TOKEN_EXTRA = "TOKEN_EXTRA"

        fun getCallingIntent(context: Context, token: String): Intent {
            val intent = Intent(context, RestoreAccountExchangeActivity::class.java)
            intent.putExtra(TOKEN_EXTRA, token)
            return intent
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    private lateinit var fragment: RestoreAccountExchangeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra(TOKEN_EXTRA) ?: ""
        viewModel.setToken(token)
        fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? RestoreAccountExchangeFragment
            ?: RestoreAccountExchangeFragment.newIntent(token).also {
                addFragment(R.id.fragmentContainer, it)
            }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        intent.getStringExtra(TOKEN_EXTRA)?.let {
            viewModel.setToken(it)

            val currentFragment = if (::fragment.isInitialized) {
                fragment
            } else {
                supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? RestoreAccountExchangeFragment
            }

            currentFragment?.let { restoreFragment ->
                fragment = restoreFragment
                restoreFragment.exchangeToken(it)
            }
        }
    }
}
