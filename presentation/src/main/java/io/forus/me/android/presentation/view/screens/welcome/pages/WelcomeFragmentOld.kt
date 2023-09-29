
package io.forus.me.android.presentation.view.screens.welcome.pages

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

/**
 * Fragment Welcome Screen.
 */
class WelcomeFragmentOld : BaseFragment() {



    override fun getLayoutID(): Int {
        return R.layout.fragment_welcome
    }


    override fun initUI() {
        restore_account.setOnClickListener {
            navigator.navigateToAccountRestore(activity)
        }

        create_account.setOnClickListener{
            navigator.navigateToAccountNew(activity)
        }
    }
}

