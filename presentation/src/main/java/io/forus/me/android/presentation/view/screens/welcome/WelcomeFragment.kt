
package io.forus.me.android.presentation.view.screens.welcome

import android.os.Bundle
import android.support.design.widget.Snackbar
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.fragment.BaseFragment
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

/**
 * Fragment Welcome Screen.
 */
class WelcomeFragment : BaseFragment() {

    companion object {
        private val ERROR_MESSAGE_EXTRA = "ERROR_MESSAGE_EXTRA"


        fun newIntent(error_message: String?): WelcomeFragment = WelcomeFragment().also {
            val bundle = Bundle()
            bundle.putString(ERROR_MESSAGE_EXTRA, error_message)
            it.arguments = bundle
        }
    }

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

        val bundle = this.arguments
        if (bundle != null) {
            var error_message = bundle.getString(WelcomeFragment.ERROR_MESSAGE_EXTRA, null)
            if (error_message != null)
                Snackbar.make(this.view!!, error_message, Snackbar.LENGTH_SHORT).show()
        }

    }
}

