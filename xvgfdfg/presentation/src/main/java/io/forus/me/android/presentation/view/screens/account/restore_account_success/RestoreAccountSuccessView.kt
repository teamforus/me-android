package io.forus.me.android.presentation.view.screens.account.restore_account_success

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by maestrovs on 22.04.2020.
 */
interface RestoreAccountSuccessView : LRView<RestoreAccountSuccessModel> {

    fun register(): io.reactivex.Observable<String>

    fun exchangeToken(): io.reactivex.Observable<String>
}