package io.forus.me.views.wallet

import io.forus.me.R
import io.forus.me.entities.Voucher
import io.forus.me.services.VoucherService

class VoucherFragment : WalletPagerFragment<Voucher>(VoucherService()) {
    override fun getLayoutResource(): Int {
        return R.layout.voucher_list_item_view
    }

}