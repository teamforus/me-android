package io.forus.me.android.presentation.view.screens.account.assigndelegates.email


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class RestoreByEmailPartialChanges : PartialChange {

    class RestoreByEmailRequestStart : RestoreByEmailPartialChanges()

    class RestoreByEmailRequestEnd : RestoreByEmailPartialChanges()

    data class RestoreByEmailRequestError(val error: Throwable) : RestoreByEmailPartialChanges()

    data class ExchangeTokenResult(val accessToken: String?) : RestoreByEmailPartialChanges()

    data class ExchangeTokenError(val error: Throwable) : RestoreByEmailPartialChanges()

}