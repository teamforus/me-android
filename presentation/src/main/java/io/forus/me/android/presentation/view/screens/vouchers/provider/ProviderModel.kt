package io.forus.me.android.presentation.view.screens.vouchers.provider

import io.forus.me.android.domain.models.vouchers.Organization
import io.forus.me.android.domain.models.vouchers.VoucherProvider
import java.math.BigDecimal

data class ProviderModel(
        val item: VoucherProvider? = null,
        val selectedOrganization: Organization? = null,
        val selectedAmount: BigDecimal = BigDecimal.ZERO,
        val sendingMakeTransaction: Boolean = false,
        val makeTransactionError: Throwable? = null
)
{

    val amountIsValid: Boolean
        get(){
            return selectedAmount >= BigDecimal("0.01")
        }

    val buttonIsActive: Boolean
        get() {
            return if(item != null && item.voucher.isProduct) true
                    else (selectedOrganization != null && amountIsValid)
        }
}