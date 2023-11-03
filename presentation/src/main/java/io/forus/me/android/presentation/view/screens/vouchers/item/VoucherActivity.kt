package io.forus.me.android.presentation.view.screens.vouchers.item


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel


class VoucherActivity : CommonActivity(), MViewModelProvider<VoucherViewModel> {

    override val viewModel: VoucherViewModel by viewModels()

    companion object {
         const val ID_EXTRA = "ID_EXTRA"
         const val VOUCHER_EXTRA = "VOUCHER_EXTRA"

        fun getCallingIntent(context: Context, id: String): Intent {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra(ID_EXTRA, id)
            return intent
        }

        fun getCallingIntent(context: Context, voucher: Voucher): Intent {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra(VOUCHER_EXTRA, voucher)
            return intent
        }
    }

    private lateinit var fragment: VoucherFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val voucher = intent.getParcelableExtra<Voucher>(VOUCHER_EXTRA)

        viewModel.setVoucher(voucher)
        viewModel.setAddress(voucher?.address?:"")

        if (savedInstanceState == null) {
            fragment = when (voucher) {
                null -> VoucherFragment.newInstance(intent.getStringExtra(ID_EXTRA)?:"")
                else -> VoucherFragment.newInstance(voucher)
            }

            addFragment(R.id.fragmentContainer, fragment)
        }
    }

}
