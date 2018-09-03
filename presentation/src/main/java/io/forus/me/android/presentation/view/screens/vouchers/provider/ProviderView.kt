package io.forus.me.android.presentation.view.screens.vouchers.provider

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.reactivex.Observable

interface ProviderView : LRView<ProviderModel> {

    fun selectAmount(): Observable<Float>

    fun submit(): Observable<Boolean>
}