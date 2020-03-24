package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.models.vouchers.VoucherProvider
import java.math.BigDecimal

data class ProductReservationModel(
        val items: List<Voucher> = emptyList()
)