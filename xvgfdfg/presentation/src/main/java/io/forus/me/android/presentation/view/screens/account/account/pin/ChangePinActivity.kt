package io.forus.me.android.presentation.view.screens.account.account.pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.ChangePinMode
import io.forus.me.android.presentation.view.activity.CommonActivity
import androidx.activity.viewModels
import io.forus.me.android.presentation.view.base.MViewModelProvider

class ChangePinActivity : CommonActivity(), MViewModelProvider<ChangePinViewModel> {

    override val viewModel: ChangePinViewModel by viewModels()

    companion object {
        private val MODE_EXTRA = "MODE_EXTRA"

        fun getCallingIntent(context: Context, mode: ChangePinMode): Intent {
            val intent = Intent(context, ChangePinActivity::class.java)
            intent.putExtra(MODE_EXTRA, mode.name)
            return intent
        }
    }



    override val viewID: Int
        get() = R.layout.activity_toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            intent.getStringExtra(MODE_EXTRA)?.let { mode ->
                viewModel.setPinMode(ChangePinMode.valueOf(mode))
                val fragment = ChangePinFragment.newIntent(ChangePinMode.valueOf(mode))
                addFragment(R.id.fragmentContainer, fragment)
            }

        }
    }
}