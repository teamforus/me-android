package io.forus.me.android.presentation.view.screens.account.newaccount


import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class NewAccountPartialChanges : PartialChange {


    data class RegisterStart(val model: NewAccountRequest) : NewAccountPartialChanges()

    data class RegisterEnd(val isSuccess: Boolean) : NewAccountPartialChanges()

    data class RegisterError(val error: Throwable) : NewAccountPartialChanges()
}