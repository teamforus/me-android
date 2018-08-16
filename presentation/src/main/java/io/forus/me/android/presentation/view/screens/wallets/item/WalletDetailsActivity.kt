package io.forus.me.android.presentation.view.screens.wallets.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

class WalletDetailsActivity : CommonActivity() {


    companion object {
        private val WALLET_ID_EXTRA = "WALLET_ID_EXTRA"

        fun getCallingIntent(context: Context, wallet: Wallet): Intent {
            val intent = Intent(context, WalletDetailsActivity::class.java)
            intent.putExtra(WALLET_ID_EXTRA, wallet.id)
            return intent
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = WalletDetailsFragment.newIntent(intent.getLongExtra(WALLET_ID_EXTRA, -1))
            addFragment(R.id.fragmentContainer, fragment)
        }
    }
}