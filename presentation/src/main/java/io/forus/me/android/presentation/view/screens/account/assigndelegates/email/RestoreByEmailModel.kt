package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import io.forus.me.android.domain.models.account.RequestDelegatesEmailModel


data class RestoreByEmailModel(
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val accessToken: String? = null
)
