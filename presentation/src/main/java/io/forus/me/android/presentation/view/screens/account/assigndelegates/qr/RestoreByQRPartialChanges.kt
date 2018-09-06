package io.forus.me.android.presentation.view.screens.account.assigndelegates.qr

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class RestoreByQRPartialChanges : PartialChange {

    class RestoreIdentity : RestoreByQRPartialChanges()

}