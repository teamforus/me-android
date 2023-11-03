package io.forus.me.android.presentation.view.screens.account.pair_device

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class PairDevicePartialChanges : PartialChange {

    class RestoreIdentity : PairDevicePartialChanges()

}