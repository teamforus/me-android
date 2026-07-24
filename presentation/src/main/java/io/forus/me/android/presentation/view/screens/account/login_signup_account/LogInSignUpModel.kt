package io.forus.me.android.presentation.view.screens.account.login_signup_account


data class LogInSignUpModel(
        val sendingEmailAuth: Boolean? = null,
        val sendingEmailAuthSuccess: Boolean? = null,
        val emailAuthError: Throwable? = null,
        val exchangeTokenError: Throwable? = null,
        val accessToken: String? = null
        )
