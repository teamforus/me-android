package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R

import io.forus.me.android.presentation.view.activity.CommonActivity
import androidx.activity.viewModels
import io.forus.me.android.presentation.view.base.MViewModelProvider

class ConfirmRegistrationActivity : CommonActivity(), MViewModelProvider<ConfirmRegistrationViewModel> {

    override val viewModel: ConfirmRegistrationViewModel by viewModels()

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
                viewModel.setToken(token)
                ConfirmRegistrationFragment.newIntent(token)
            } else ConfirmRegistrationFragment()
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.getStringExtra(TOKEN_EXTRA)?.let{
    fragment.exchangeToken(it)
}
    }
}
