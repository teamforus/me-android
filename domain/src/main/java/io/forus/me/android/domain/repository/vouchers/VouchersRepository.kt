package io.forus.me.android.domain.repository.vouchers

import io.forus.me.android.domain.models.common.Page
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.domain.models.vouchers.Voucher
import io.reactivex.Observable

interface VouchersRepository {

    fun getVouchers(): Observable<List<Voucher>>


    fun getVoucher(id: String): Observable<Voucher>

    fun getTransactions(voucherId: String): Observable<Page<Transaction>>
}
