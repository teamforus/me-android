package io.forus.me.android.presentation.view.screens.vouchers.provider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

class ProviderActivity : CommonActivity() {


    companion object {

        val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        val IS_DEMO_VOUCHER = "IS_DEMO_VOUCHER"

        fun getCallingIntent(context: Context, id: String, isDemoVoucher: Boolean? = false): Intent {
            val intent = Intent(context, ProviderActivity::class.java)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, id)
            if(isDemoVoucher != null)intent.putExtra(IS_DEMO_VOUCHER, isDemoVoucher)
            return intent
        }
    }


    override val viewID: Int
        get() = R.layout.activity_toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = ProviderFragment.newIntent(intent.getStringExtra(VOUCHER_ADDRESS_EXTRA), intent.getBooleanExtra(IS_DEMO_VOUCHER, false))

            addFragment(R.id.fragmentContainer, fragment)
        }
    }


}