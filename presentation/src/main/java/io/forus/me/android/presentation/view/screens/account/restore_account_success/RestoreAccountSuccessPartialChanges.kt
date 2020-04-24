package io.forus.me.android.presentation.view.screens.account.restore_account_success


import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesEmailModel


sealed class RestoreAccountSuccessPartialChanges : PartialChange {

    class RestoreByEmailRequestStart : RestoreAccountSuccessPartialChanges()

    data class RestoreByEmailRequestEnd(val accessToken: String?) : RestoreAccountSuccessPartialChanges()

    data class RestoreByEmailRequestError(val error: Throwable) : RestoreAccountSuccessPartialChanges()

    data class ExchangeTokenResult(val accessToken: String?) : RestoreAccountSuccessPartialChanges()

    data class ExchangeTokenError(val error: Throwable) : RestoreAccountSuccessPartialChanges()

}