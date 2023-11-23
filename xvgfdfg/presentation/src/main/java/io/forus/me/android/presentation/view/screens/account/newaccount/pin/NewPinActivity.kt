package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity
import androidx.activity.viewModels
import io.forus.me.android.presentation.view.base.MViewModelProvider

class NewPinActivity : CommonActivity(), MViewModelProvider<NewPinViewModel> {

    override val viewModel: NewPinViewModel by viewModels()

    companion object {
        private val ACCESS_TOKEN_EXTRA = "ACCESS_TOKEN_EXTRA"

        fun getCallingIntent(context: Context, accessToken: String): Intent {
            val intent = Intent(context, NewPinActivity::class.java)
            intent.putExtra(ACCESS_TOKEN_EXTRA, accessToken)
            return intent
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (savedInstanceState == null) {
            intent.getStringExtra(ACCESS_TOKEN_EXTRA)?.let { accessToken ->

                viewModel.setAccessToken(accessToken)

                val fragment = NewPinFragment.newIntent(accessToken)
                addFragment(R.id.fragmentContainer, fragment)
            }

        }
    }
}