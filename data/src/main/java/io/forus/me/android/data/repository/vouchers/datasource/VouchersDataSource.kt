package io.forus.me.android.data.repository.vouchers.datasource

import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.entity.vouchers.response.Transaction
import io.forus.me.android.data.entity.vouchers.response.Voucher
import io.reactivex.Observable

interface VouchersDataSource {

    fun listAllVouchers(): Observable<List<Voucher>>

    fun retrieveVoucher(address: String): Observable<Voucher>

    fun retrieveProductVouchersAsProvider(address: String): Observable<List<Voucher>>

    fun retrieveVoucherAsProvider(address: String): Observable<Voucher>

    fun makeTransaction(address: String, makeTransaction: MakeTransaction): Observable<Transaction>

    fun sendEmail(address: String): Observable<Boolean>

}