package io.forus.me.android.presentation.view.screens.lock.pin

import io.forus.me.android.presentation.view.base.lr.LRView

interface PinLockView : LRView<PinLockModel> {

    fun pinOnComplete(): io.reactivex.Observable<String>

    fun pinOnChange(): io.reactivex.Observable<String>

    fun exit(): io.reactivex.Observable<Unit>

}