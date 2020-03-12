package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

class ProductReservationActivity : CommonActivity() {


    companion object {

        val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"

        fun getCallingIntent(context: Context, id: String): Intent {
            val intent = Intent(context, ProductReservationActivity::class.java)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, id)
            return intent
        }
    }


    override val viewID: Int
        get() = R.layout.activity_toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = ProductReservationFragment.newIntent(intent.getStringExtra(VOUCHER_ADDRESS_EXTRA))

            addFragment(R.id.fragmentContainer, fragment)
        }
    }


}