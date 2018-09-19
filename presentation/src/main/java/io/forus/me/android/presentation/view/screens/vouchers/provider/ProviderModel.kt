package io.forus.me.android.presentation.view.screens.vouchers.provider

import io.forus.me.android.domain.models.vouchers.Organization
import io.forus.me.android.domain.models.vouchers.VoucherProvider

data class ProviderModel(
        val item: VoucherProvider? = null,
        val selectedOrganization: Organization? = null,
        val selectedAmount: Float = Float.NaN,
        val sendingMakeTransaction: Boolean = false,
        val makeTransactionError: Throwable? = null
)
{
    val buttonIsActive: Boolean
        get() {
            return selectedOrganization != null && selectedAmount != Float.NaN && selectedAmount >= 0.01 && selectedAmount <= item!!.voucher.amount.toFloat()
        }
}