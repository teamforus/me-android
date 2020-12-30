package io.forus.me.android.presentation.view.screens.account.login_signup_account


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class LogInSignUpPartialChanges : PartialChange {

    class RestoreByEmailRequestStart : LogInSignUpPartialChanges()

    class RestoreByEmailRequestEnd : LogInSignUpPartialChanges()

    data class ValidateEmailRequest(val validateEmail: io.forus.me.android.domain.models.account.ValidateEmail) : LogInSignUpPartialChanges()

    data class RestoreByEmailRequestError(val error: Throwable) : LogInSignUpPartialChanges()

    data class ExchangeTokenResult(val accessToken: String?) : LogInSignUpPartialChanges()

    data class ExchangeTokenError(val error: Throwable) : LogInSignUpPartialChanges()


    data class ValidateEmailRequestError(val error: Throwable) : LogInSignUpPartialChanges()

}