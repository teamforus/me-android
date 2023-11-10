package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface RestoreByEmailView : LRView<RestoreByEmailModel> {

    fun register(): io.reactivex.Observable<String>

    fun exchangeToken(): io.reactivex.Observable<String>
}