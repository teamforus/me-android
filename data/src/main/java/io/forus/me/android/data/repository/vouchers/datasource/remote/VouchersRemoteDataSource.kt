package io.forus.me.android.data.repository.vouchers.datasource.remote

import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.entity.vouchers.response.Transaction
import io.forus.me.android.data.entity.vouchers.response.Voucher
import io.forus.me.android.data.net.vouchers.VouchersService
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.forus.me.android.data.repository.vouchers.datasource.VouchersDataSource
import io.reactivex.Observable

class VouchersRemoteDataSource(f: () -> VouchersService): VouchersDataSource, RemoteDataSource<VouchersService>(f) {


    override fun listAllVouchers(): Observable<List<Voucher>> {
        return service.listAllVouchers().map { it.data }
    }

    override fun retrieveVoucher(address: String): Observable<Voucher> {
        return service.getVoucher(address).map { it.data }
    }

    override fun retrieveProductVouchersAsProvider(address: String): Observable<List<Voucher>> {
        return service.getProductVouchersAsProvider(address).map { it.data }
    }

    override fun retrieveVoucherAsProvider(address: String): Observable<Voucher> {
        return service.getVoucherAsProvider(address).map { it.data }
    }

    override fun makeTransaction(address: String, makeTransaction: MakeTransaction): Observable<Transaction> {
        return service.makeTransaction(address, makeTransaction).map { it.data }
    }

    override fun sendEmail(address: String): Observable<Boolean> {
        return service.sendEmail(address).map { _: Void? -> true }
    }
}