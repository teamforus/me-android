package io.forus.me.android.presentation.view.screens.vouchers.list;

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.vouchers.Voucher

data class VouchersModel(
        val items: List<Voucher> = emptyList()
        ) {
}