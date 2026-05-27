package io.forus.me.android.presentation.view.screens.account.restore_account_exchange

import io.forus.me.android.presentation.view.base.lr.LRView
import io.reactivex.Observable

interface RestoreAccountExchangeView : LRView<RestoreAccountExchangeModel> {

    fun exchangeToken(): Observable<String>
}
