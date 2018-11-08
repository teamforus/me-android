package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface VoucherView : LRView<VoucherModel>{

    fun sendEmail(): io.reactivex.Observable<Unit>

    fun sendEmailDialogShown(): io.reactivex.Observable<Unit>
}