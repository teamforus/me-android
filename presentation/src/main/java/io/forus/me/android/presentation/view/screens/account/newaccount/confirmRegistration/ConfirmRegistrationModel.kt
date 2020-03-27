package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration


data class ConfirmRegistrationModel(
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailSuccess: Boolean? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val exchangeTokenError: Throwable? = null,
        val accessToken: String? = null
)
