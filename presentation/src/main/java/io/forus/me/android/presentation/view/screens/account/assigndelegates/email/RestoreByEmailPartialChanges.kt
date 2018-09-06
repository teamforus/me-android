package io.forus.me.android.presentation.view.screens.account.assigndelegates.email


import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesEmailModel

sealed class RestoreByEmailPartialChanges : PartialChange {

    class RestoreIdentity(): RestoreByEmailPartialChanges()

    class RestoreByEmailRequestStart() : RestoreByEmailPartialChanges()

    data class RestoreByEmailRequestEnd(val requestDelegatesEmailModel: RequestDelegatesEmailModel) : RestoreByEmailPartialChanges()

    data class RestoreByEmailRequestError(val error: Throwable) : RestoreByEmailPartialChanges()

}