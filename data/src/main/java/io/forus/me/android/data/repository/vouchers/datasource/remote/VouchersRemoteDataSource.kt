package io.forus.me.android.data.repository.vouchers.datasource.remote

import io.forus.me.android.data.entity.vouchers.request.MakeActionTransaction
import io.forus.me.android.data.entity.vouchers.request.MakeDemoTransaction
import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.entity.vouchers.response.DemoTransaction
import io.forus.me.android.data.entity.vouchers.response.ProductAction
import io.forus.me.android.data.entity.vouchers.response.Transaction
import io.forus.me.android.data.entity.vouchers.response.Voucher
import io.forus.me.android.data.net.vouchers.VouchersService
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.forus.me.android.data.repository.vouchers.datasource.VouchersDataSource
import io.reactivex.Observable
import okhttp3.ResponseBody

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

    override fun retrieveVoucherProductsActionsAsProvider(address: String, organizationId: Long, page: Int, perPage: Int): Observable<List<ProductAction>> {
        return service.getActionProductsOfVoucherAsProvider(address, organization_id = organizationId.toString(),
                page = page.toString(), perPage = perPage.toString()).map { it.data }
    }

    override fun retrieveVoucherAsProvider(address: String): Observable<Voucher> {
        return service.getVoucherAsProvider(address).map { it.data }
    }

    override fun makeTransaction(address: String, makeTransaction: MakeTransaction): Observable<Transaction> {
        return service.makeTransaction(address, makeTransaction).map { it.data }
    }

    override fun makeActionTransaction(address: String, makeTransaction: MakeActionTransaction): Observable<Transaction> {
        return service.makeActionTransaction(address, makeTransaction).map { it.data }
    }



    override fun sendEmail(address: String): Observable<Boolean> {
        return service.sendEmail(address).map { _: Void? -> true }
    }

    override fun makeDemoTransaction(address: String, makeDemoTransaction: MakeDemoTransaction): Observable<DemoTransaction> {
        return service.demoVoucher(address, makeDemoTransaction).map { it }
    }
}