package io.forus.me.android.presentation.view.screens.account.login_signup_account


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class LogInSignUpPartialChanges : PartialChange {

    class EmailAuthRequestStart : LogInSignUpPartialChanges()

    class EmailAuthRequestEnd : LogInSignUpPartialChanges()

    data class EmailAuthRequestError(val error: Throwable) : LogInSignUpPartialChanges()

    data class ExchangeTokenResult(val accessToken: String?) : LogInSignUpPartialChanges()

    data class ExchangeTokenError(val error: Throwable) : LogInSignUpPartialChanges()
}
