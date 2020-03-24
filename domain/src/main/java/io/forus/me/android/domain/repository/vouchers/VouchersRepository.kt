package io.forus.me.android.domain.repository.vouchers

import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.models.vouchers.VoucherProvider
import io.reactivex.Observable
import java.math.BigDecimal

interface VouchersRepository {

    fun getVouchers(): Observable<List<Voucher>>

    fun getVoucher(address: String): Observable<Voucher>

    fun getVoucherAsProvider(address: String): Observable<VoucherProvider>

    fun getProductVouchersAsProvider(address: String): Observable<List<Voucher>>

    fun makeTransaction(address: String, amount: BigDecimal, note: String, organizationId: Long): Observable<Boolean>

    fun sendEmail(address: String): Observable<Boolean>
}
