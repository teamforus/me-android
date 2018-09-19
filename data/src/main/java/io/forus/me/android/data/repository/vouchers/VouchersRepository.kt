package io.forus.me.android.data.repository.vouchers

import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.repository.vouchers.datasource.VouchersDataSource
import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.vouchers.*
import io.reactivex.Observable

class VouchersRepository(private val vouchersDataSource: VouchersDataSource) : io.forus.me.android.domain.repository.vouchers.VouchersRepository {

    private fun mapToSimple(voucher: io.forus.me.android.data.entity.vouchers.response.Voucher): Voucher{
        val euro = Currency("€")
        val logoUrl = voucher.fund?.logo?.sizes?.large ?: (voucher.fund?.organization?.logo?.sizes?.large ?: "https://freeiconshop.com/wp-content/uploads/edd/person-flat.png")
        val transactions = voucher.transactions.map { Transaction(it.address, it.organization.name, euro, it.amount, it.dateTime) }
        return Voucher(voucher.address, voucher.fund.name, 2, euro, voucher.amount, logoUrl, transactions)
    }

    private fun mapToProvider(voucher: io.forus.me.android.data.entity.vouchers.response.Voucher): VoucherProvider{
        val euro = Currency("€")
        val logoUrl = voucher.fund?.logo?.sizes?.large ?: (voucher.fund?.organization?.logo?.sizes?.large ?: "https://freeiconshop.com/wp-content/uploads/edd/person-flat.png")
        val item = Voucher(voucher.address, voucher.fund.name, 2, euro, voucher.amount, logoUrl, emptyList())
        val categoris = voucher.allowedProductCategories.map { ProductCategory(it.id, it.key, it.name) }
        val organizations = voucher.allowedOrganizations.map { Organization(it.id, it.name) }
        return VoucherProvider(item, organizations, categoris)
    }

    override fun getVouchers(): Observable<List<Voucher>> {
        return vouchersDataSource.listAllVouchers().map { it.map { mapToSimple(it) } }
    }

    override fun getVoucher(address: String): Observable<Voucher> {
        return vouchersDataSource.retrieveVoucher(address).map { mapToSimple(it) }
    }

    override fun getVoucherAsProvider(address: String): Observable<VoucherProvider> {
        return vouchersDataSource.retrieveVoucherAsProvider(address).map { mapToProvider(it) }
    }

    override fun makeTransaction(address: String, amount: Float, organizationId: Long): Observable<Boolean> {
        return vouchersDataSource.makeTransaction(address, MakeTransaction(amount, organizationId)).map { true }
    }

}