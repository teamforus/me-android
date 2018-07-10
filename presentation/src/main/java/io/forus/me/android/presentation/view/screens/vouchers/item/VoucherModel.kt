package io.forus.me.android.presentation.view.screens.vouchers.item;

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Voucher

data class VoucherModel(
        val item: Voucher? = null,
        val transactions: List<Transaction> = emptyList()
        ) {
}