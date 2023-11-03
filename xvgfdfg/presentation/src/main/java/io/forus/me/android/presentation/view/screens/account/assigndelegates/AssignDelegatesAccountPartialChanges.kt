package io.forus.me.android.presentation.view.screens.account.assigndelegates

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class AssignDelegatesAccountPartialChanges : PartialChange {

    class RestoreIdentity : AssignDelegatesAccountPartialChanges()

}