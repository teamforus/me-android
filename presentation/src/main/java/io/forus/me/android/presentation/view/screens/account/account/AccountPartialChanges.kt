package io.forus.me.android.presentation.view.screens.account.account


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class AccountPartialChanges : PartialChange {

    data class FingerprintEnabled(val value: Boolean) : AccountPartialChanges()

    data class StartFromScannerEnabled(val value: Boolean) : AccountPartialChanges()

    data class NavigateToWelcomeScreen(val value: Boolean) : AccountPartialChanges()

    data class SendCrashReportsEnabled(val value: Boolean) : AccountPartialChanges()

}