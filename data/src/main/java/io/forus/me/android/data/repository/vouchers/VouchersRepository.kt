package io.forus.me.android.data.repository.vouchers

import io.forus.me.android.data.entity.vouchers.request.MakeTransaction
import io.forus.me.android.data.repository.vouchers.datasource.VouchersDataSource
import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.vouchers.*
import io.reactivex.Observable
import java.lang.Exception
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class VouchersRepository(private val vouchersDataSource: VouchersDataSource) : io.forus.me.android.domain.repository.vouchers.VouchersRepository {

    val dateLocaleFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US)

    private fun mapToSimple(voucher: io.forus.me.android.data.entity.vouchers.response.Voucher): Voucher{
        val isProduct = voucher.type == io.forus.me.android.data.entity.vouchers.response.Voucher.Type.product
        val isUsed = isProduct && voucher.transactions !=null && voucher.transactions.isNotEmpty()

        val name = if(isProduct) voucher.product.name else voucher.fund.name
        val organizationName = if(isProduct) voucher.product.organization.name else voucher.fund.organization.name

        var createdAt: Date? = null
        try{
            if(voucher.createdAtLocale != null) createdAt = dateLocaleFormat.parse(voucher.createdAtLocale)
        }
        catch (e: Exception){

        }
        if(createdAt == null) createdAt = (if(voucher.createdAt != null) voucher.createdAt else (Date(voucher.timestamp * 1000)))

        val description = if(isProduct) voucher.product.description else null

        val amount = voucher.amount ?: voucher.product.price
        val euro = Currency("€")
        val logoUrl = voucher.product?.photo?.sizes?.large
                ?: (voucher.product?.organization?.logo?.sizes?.large
                    ?: ((voucher.fund?.logo?.sizes?.large
                            ?: (voucher.fund?.organization?.logo?.sizes?.large
                                    ?: ""))))

        val transactions = if(voucher.transactions == null) emptyList()
                else voucher.transactions.map { Transaction(it.address, Organization(it.organization.id, it.organization.name, it.organization?.logo?.sizes?.large ?: ""), euro, it.amount, it.createdAt) }

        return Voucher(isProduct, isUsed, voucher.address ?: "", name, organizationName, voucher.fund.name, description, createdAt!!, euro, amount, logoUrl, transactions)
    }

    private fun mapToProvider(voucher: io.forus.me.android.data.entity.vouchers.response.Voucher): VoucherProvider{
        val item = mapToSimple(voucher)
        val categories = if(voucher.allowedProductCategories == null) emptyList()
                            else voucher.allowedProductCategories.map { ProductCategory(it.id, it.key, it.name) }
        val organizations = if(voucher.allowedOrganizations == null) emptyList()
                                else voucher.allowedOrganizations.map { Organization(it.id, it.name, it.logo?.sizes?.large ?: "") }
        return VoucherProvider(item, organizations, categories)
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

    override fun makeTransaction(address: String, amount: BigDecimal, organizationId: Long): Observable<Boolean> {
        return vouchersDataSource.makeTransaction(address, MakeTransaction(amount, organizationId)).map { true }
    }

}