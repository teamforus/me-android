package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel
import androidx.activity.viewModels

class ProductReservationActivity : CommonActivity(), MViewModelProvider<VoucherViewModel> {

    override val viewModel: VoucherViewModel by viewModels()

    companion object {

        val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"
        val SHOW_PARENT_VOUCHER = "SHOW_PARENT_VOUCHER"

        fun getCallingIntent(context: Context, id: String, showParentVoucher: Boolean): Intent {
            val intent = Intent(context, ProductReservationActivity::class.java)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, id)
            intent.putExtra(SHOW_PARENT_VOUCHER, showParentVoucher)
            return intent
        }
    }


    override val viewID: Int
        get() = R.layout.activity_toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val voucherAddress = intent.getStringExtra(VOUCHER_ADDRESS_EXTRA)?:""
            viewModel.setAddress(voucherAddress)
            val fragment = ProductReservationFragment.newIntent(voucherAddress, intent.getBooleanExtra(SHOW_PARENT_VOUCHER, false));

            addFragment(R.id.fragmentContainer, fragment)
        }
    }


}