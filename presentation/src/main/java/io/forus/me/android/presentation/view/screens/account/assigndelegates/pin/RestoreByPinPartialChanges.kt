package io.forus.me.android.presentation.view.screens.account.assigndelegates.pin


import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class RestoreByPinPartialChanges : PartialChange {

    class RestoreIdentity() : RestoreByPinPartialChanges()

}