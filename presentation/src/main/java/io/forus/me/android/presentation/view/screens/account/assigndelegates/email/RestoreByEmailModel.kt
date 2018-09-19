package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import io.forus.me.android.domain.models.account.RequestDelegatesEmailModel


data class RestoreByEmailModel(
        val item: RequestDelegatesEmailModel? = null,
        val sendingRestoreByEmail: Boolean? = null,
        val sendingRestoreByEmailError: Throwable? = null,
        val isEmailConfirmed: Boolean = false
)
