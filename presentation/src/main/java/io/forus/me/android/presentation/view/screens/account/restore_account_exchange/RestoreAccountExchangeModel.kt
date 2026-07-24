package io.forus.me.android.presentation.view.screens.account.restore_account_exchange

data class RestoreAccountExchangeModel(
    val exchangingToken: Boolean? = null,
    val exchangeTokenError: Throwable? = null,
    val accessToken: String? = null
)
