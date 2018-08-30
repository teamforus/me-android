package io.forus.me.android.presentation.view.screens.vouchers.provider

import io.forus.me.android.domain.models.vouchers.Organization
import io.forus.me.android.domain.models.vouchers.VoucherProvider

data class ProviderModel(
        val item: VoucherProvider? = null,
        val selectedOrganization: Organization? = null,
        val selectedAmount: Float? = null,
        val sendingMakeTransaction: Boolean = false,
        val makeTransactionError: Throwable? = null
)
{
    val buttonIsActive: Boolean
        get() {
            return selectedOrganization != null && selectedAmount != null && selectedAmount > 0 && selectedAmount <= item!!.voucher.amount.toFloat()
        }
}