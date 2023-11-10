package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration


import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesEmailModel

sealed class ConfirmRegistrationPartialChanges : PartialChange {

    class RestoreByEmailRequestStart : ConfirmRegistrationPartialChanges()

    class RestoreByEmailRequestEnd : ConfirmRegistrationPartialChanges()

    data class RestoreByEmailRequestError(val error: Throwable) : ConfirmRegistrationPartialChanges()

    data class ExchangeTokenResult(val accessToken: String?) : ConfirmRegistrationPartialChanges()

    data class ExchangeTokenError(val error: Throwable) : ConfirmRegistrationPartialChanges()

}