package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import io.forus.me.android.presentation.models.vouchers.Voucher

data class ProductReservationModel(
        val items: List<Voucher> = emptyList()
)