
package io.forus.me.android.presentation.view.screens.welcome

import android.os.Bundle
import android.view.View
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.welcome_fragment.*

/**
 * Fragment Welcome Screen.
 */
class WelcomeFragment : BaseFragment() {



    override fun getLayoutID(): Int {
        return R.layout.welcome_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

