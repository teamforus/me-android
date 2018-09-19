package io.forus.me.android.presentation.view.screens.vouchers.provider

import io.forus.me.android.presentation.view.base.lr.LRView
import io.reactivex.Observable

interface ProviderView : LRView<ProviderModel> {

    fun selectAmount(): Observable<Float>

    fun submit(): Observable<Boolean>
}