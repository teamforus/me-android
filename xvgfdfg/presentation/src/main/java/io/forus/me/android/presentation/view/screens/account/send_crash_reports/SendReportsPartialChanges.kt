package io.forus.me.android.presentation.view.screens.account.send_crash_reports


import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesEmailModel


sealed class SendReportsPartialChanges : PartialChange {

    data class SendCrashReportsEnabled(val value: Boolean) : SendReportsPartialChanges()

}