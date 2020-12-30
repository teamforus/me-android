package io.forus.me.android.presentation.view.screens.account.login_signup_account


data class LogInSignUpModel(
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailSuccess: Boolean? = null,
        val validateEmail: io.forus.me.android.domain.models.account.ValidateEmail? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val exchangeTokenError: Throwable? = null,
        val accessToken: String? = null,
        val validateEmailError: Throwable? = null
        )