package io.forus.me.android.presentation.view.screens.account.assigndelegates.pin


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class RestoreByPinPartialChanges : PartialChange {

    class RestoreIdentity() : RestoreByPinPartialChanges()

}