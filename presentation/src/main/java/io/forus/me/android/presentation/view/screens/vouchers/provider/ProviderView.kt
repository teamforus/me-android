package io.forus.me.android.presentation.view.screens.vouchers.provider

import io.forus.me.android.presentation.view.base.lr.LRView
import io.reactivex.Observable
import java.math.BigDecimal

interface ProviderView : LRView<ProviderModel> {

    fun selectAmount(): Observable<BigDecimal>

    fun charge(): Observable<BigDecimal>
}