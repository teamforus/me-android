package io.forus.me.android.presentation.view.screens.pinlock

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView

interface PinLockView : LRView<PinLockModel> {

    fun pinOnComplete(): io.reactivex.Observable<String>

    fun pinOnChange(): io.reactivex.Observable<String>

    fun exit(): io.reactivex.Observable<Unit>

}