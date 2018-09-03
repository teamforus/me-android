package io.forus.me.android.presentation.view.screens.account.account.pin

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView

interface ChangePinView : LRView<ChangePinModel> {

    fun pinOnComplete(): io.reactivex.Observable<String>

    fun pinOnChange(): io.reactivex.Observable<String>

}