package io.forus.me.android.presentation.view.screens.account.restore_account_exchange

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class RestoreAccountExchangePartialChanges : PartialChange {

    class ExchangeTokenStart : RestoreAccountExchangePartialChanges()

    data class ExchangeTokenResult(val accessToken: String?) : RestoreAccountExchangePartialChanges()

    data class ExchangeTokenError(val error: Throwable) : RestoreAccountExchangePartialChanges()
}
