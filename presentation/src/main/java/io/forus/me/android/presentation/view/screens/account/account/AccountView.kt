package io.forus.me.android.presentation.view.screens.account.account

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface AccountView : LRView<AccountModel> {



    fun logout(): io.reactivex.Observable<Boolean>

    fun switchFingerprint(): io.reactivex.Observable<Boolean>

    fun switchStartFromScanner(): io.reactivex.Observable<Boolean>

    fun switchSendCrashReports(): io.reactivex.Observable<Boolean>

    fun refreshDataIntent(): io.reactivex.Observable<Unit>
}