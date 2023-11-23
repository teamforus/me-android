package io.forus.me.android.data.repository.vouchers.datasource

import io.forus.me.android.data.entity.vouchers.request.MakeActionTransaction
import io.forus.me.android.data.entity.vouchers.request.MakeDemoTransaction
import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.entity.vouchers.response.DemoTransaction
import io.forus.me.android.data.entity.vouchers.response.ProductAction
import io.forus.me.android.data.entity.vouchers.response.Transaction
import io.forus.me.android.data.entity.vouchers.response.Voucher
import io.reactivex.Observable
import okhttp3.ResponseBody

interface VouchersDataSource {

    fun listAllVouchers(): Observable<List<Voucher>>

    fun retrieveVoucher(address: String): Observable<Voucher>

    fun retrieveProductVouchersAsProvider(address: String): Observable<List<Voucher>>

    fun retrieveVoucherProductsActionsAsProvider(address: String, organizationId: Long, page: Int, perPage: Int): Observable<List<ProductAction>>

    fun retrieveTransactionsLogAsProvider(from: String, page: Int, perPage: Int): Observable<List<Transaction>>

    fun retrieveVoucherAsProvider(address: String): Observable<Voucher>

    fun makeTransaction(address: String, makeTransaction: MakeTransaction): Observable<Transaction>

    fun makeActionTransaction(address: String, makeTransaction: MakeActionTransaction): Observable<Transaction>

    fun sendEmail(address: String): Observable<Boolean>

    fun makeDemoTransaction(testToken: String, makeDemoTransaction: MakeDemoTransaction): Observable<DemoTransaction>

}