package io.forus.me.android.presentation.view.screens.vouchers.item


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class VoucherPartialChanges : PartialChange{

    data class SendEmailSuccess(val void: Unit) : VoucherPartialChanges()
    data class SendEmailError(val error: Throwable) : VoucherPartialChanges()
    data class SendEmailDialogShown(val void: Unit) : VoucherPartialChanges()
}