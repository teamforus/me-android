package io.forus.me.android.presentation.view.screens.account.send_crash_reports

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by maestrovs on 23.04.2020.
 */
interface SendReportsView : LRView<SendReportsModel> {

    fun switchSendCrashReports(): io.reactivex.Observable<Boolean>
}