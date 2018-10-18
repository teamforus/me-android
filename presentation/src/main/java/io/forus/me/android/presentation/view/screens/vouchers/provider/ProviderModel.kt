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

    val amountIsValid: Boolean
        get(){
            return selectedAmount != Float.NaN && selectedAmount >= 0.01 //&& selectedAmount <= item!!.voucher.amount.toFloat()
        }

    val buttonIsActive: Boolean
        get() {
            return if(item != null && item.voucher.isProduct) true
                    else (selectedOrganization != null && amountIsValid)
        }
}