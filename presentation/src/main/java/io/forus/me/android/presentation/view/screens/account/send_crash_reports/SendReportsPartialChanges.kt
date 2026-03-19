package io.forus.me.android.presentation.view.screens.account.send_crash_reports


import io.forus.me.android.presentation.view.base.lr.PartialChange


sealed class SendReportsPartialChanges : PartialChange {

    data class SendCrashReportsEnabled(val value: Boolean) : SendReportsPartialChanges()

}