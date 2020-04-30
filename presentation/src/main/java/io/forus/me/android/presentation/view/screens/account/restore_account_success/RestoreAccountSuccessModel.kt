package io.forus.me.android.presentation.view.screens.account.restore_account_success

/**
 * Created by maestrovs on 22.04.2020.
 */
data class RestoreAccountSuccessModel(
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailSuccess: Boolean? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val exchangeTokenError: Throwable? = null,
        val accessToken: String? = null
)
