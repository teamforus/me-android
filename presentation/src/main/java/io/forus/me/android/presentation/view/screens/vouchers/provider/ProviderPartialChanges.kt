package io.forus.me.android.presentation.view.screens.vouchers.provider

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class ProviderPartialChanges : PartialChange {

    data class SetAmount(val amount: Float) : ProviderPartialChanges()

    class MakeTransactionStart() : ProviderPartialChanges()

    class MakeTransactionEnd() : ProviderPartialChanges()

    data class MakeTransactionError(val error: Throwable) : ProviderPartialChanges()

}