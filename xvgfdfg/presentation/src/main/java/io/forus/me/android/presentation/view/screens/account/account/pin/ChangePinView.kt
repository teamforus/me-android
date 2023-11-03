package io.forus.me.android.presentation.view.screens.account.account.pin

import io.forus.me.android.presentation.view.base.lr.LRView

interface ChangePinView : LRView<ChangePinModel> {

    fun pinOnComplete(): io.reactivex.Observable<String>

    fun pinOnChange(): io.reactivex.Observable<String>

}