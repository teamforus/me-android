package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.presentation.models.vouchers.Voucher

data class VoucherModel(
        val item: Voucher? = null,
        val emailSend: EmailSend = EmailSend.NOTHING
)

enum class EmailSend {
    SEND,
    SENT,
    NOTHING
}