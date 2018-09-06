package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import io.forus.me.android.presentation.view.base.lr.LRView

interface NewPinView : LRView<NewPinModel> {

    fun pinOnComplete(): io.reactivex.Observable<String>

    fun pinOnChange(): io.reactivex.Observable<String>

    fun skip(): io.reactivex.Observable<Unit>

}