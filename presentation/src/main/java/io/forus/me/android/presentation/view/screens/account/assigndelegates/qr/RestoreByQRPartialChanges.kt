package io.forus.me.android.presentation.view.screens.account.assigndelegates.qr

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class RestoreByQRPartialChanges : PartialChange {

    class RestoreIdentity() : RestoreByQRPartialChanges()

}