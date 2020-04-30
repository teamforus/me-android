package io.forus.me.android.presentation.view.screens.vouchers.provider

import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.view.base.lr.PartialChange
import java.math.BigDecimal

sealed class ProviderPartialChanges : PartialChange {

    data class SetAmount(val amount: BigDecimal) : ProviderPartialChanges()

    data class SetNote(val note: String) : ProviderPartialChanges()

    class MakeTransactionStart : ProviderPartialChanges()

    class MakeTransactionEnd : ProviderPartialChanges()

    data class MakeTransactionError(val error: Throwable) : ProviderPartialChanges()

    data class SelectOrganization(val organization: Organization) : ProviderPartialChanges()

}