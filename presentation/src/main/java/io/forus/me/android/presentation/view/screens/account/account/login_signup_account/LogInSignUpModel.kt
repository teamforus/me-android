package io.forus.me.android.presentation.view.screens.account.account.login_signup_account

import io.forus.me.android.domain.models.account.Account


data class LogInSignUpModel(
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailSuccess: Boolean? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val exchangeTokenError: Throwable? = null,
        val accessToken: String? = null
        )