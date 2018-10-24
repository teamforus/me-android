package io.forus.me.android.presentation.view.screens.account.assigndelegates.email


data class RestoreByEmailModel(
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailSuccess: Boolean? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val exchangeTokenError: Throwable? = null,
        val accessToken: String? = null
)
