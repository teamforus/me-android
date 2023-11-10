package io.forus.me.android.presentation.view.screens.account.newaccount

import io.forus.me.android.domain.models.account.NewAccountRequest


data class NewAccountModel(
        val item: NewAccountRequest = NewAccountRequest(),
        val sendingRegistration: Boolean = false,
        val sendingRegistrationError: Throwable? = null,
        val isSuccess: Boolean? = null
        )