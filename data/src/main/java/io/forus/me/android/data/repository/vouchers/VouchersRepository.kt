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

    private fun mapToSimple(voucher: io.forus.me.android.data.entity.vouchers.response.Voucher): Voucher {
        val isProduct = voucher.type == io.forus.me.android.data.entity.vouchers.response.Voucher.Type.product
        val isUsed = isProduct && voucher.transactions != null && voucher.transactions.isNotEmpty()

        val name = if (isProduct) voucher.product.name else voucher.fund.name
        val organizationName = if (isProduct) voucher.product.organization.name else voucher.fund.organization.name

        var createdAt: Date? = null
        try {
            if (voucher.createdAtLocale != null) createdAt = dateLocaleFormat.parse(voucher.createdAtLocale)
        } catch (e: Exception) {

        }

        if (createdAt == null) {
            createdAt =
                    if (voucher.createdAt != null) voucher.createdAt
                    else (Date(voucher.timestamp * 1000))
        }

        val description = if (isProduct) voucher.product.description else null

        val product = voucher.product
        val amount = voucher.amount ?: voucher.product.price
        val euro = Currency("€")
        val productLogoUrl = voucher.product?.photo?.sizes?.large
                ?: (voucher.product?.organization?.logo?.sizes?.large
                        ?: ((voucher.fund?.logo?.sizes?.large
                                ?: (voucher.fund?.organization?.logo?.sizes?.large
                                        ?: ""))))

        val organizationLogoUrl = voucher.product?.organization?.logo?.sizes?.large ?: ""

        val transactions = mutableListOf<Transaction>()
        if (voucher.transactions != null) {
            transactions.addAll(voucher.transactions.map {
                Transaction(it.address, Organization(it.organization.id, it.organization.name, it.organization?.logo?.sizes?.large
                        ?: "", it.organization?.lat ?: 0.0, it.organization?.lon
                        ?: 0.0, it.organization?.identityAddress ?: "", it.organization?.phone
                        ?: "", it.organization?.email ?: ""), euro, it.amount, it.createdAt)
            })
        }

        if (voucher.childVouchers != null) {
            transactions.addAll(voucher.childVouchers.map { childVoucher ->
                val organization = childVoucher.product?.organization
                Transaction("", Organization(childVoucher.product.organizationId, childVoucher.product.name, "", organization?.lat
                        ?: 0.0, organization?.lon ?: 0.0, organization?.identityAddress
                        ?: "", organization?.phone ?: "", organization?.email
                        ?: ""), euro, childVoucher.amount, childVoucher.createdAt, Transaction.Type.Product)
            })
        }
        transactions.sortWith(Comparator { t1, t2 ->
            -t1.createdAt.compareTo(t2.createdAt)
        })

        var productMapped: Product? = null
        product?.let {

            var organization = Organization(it.organization.id,
                    it.organization.name, organizationLogoUrl, it.organization.lat,
                    it.organization.lon, it.organization.identityAddress ?: "",
                    it.organization.phone ?: "", it.organization.email ?: "")

            voucher.offices?.map { office ->
                if (it.organizationId == office.organizationId) {
                    organization = Organization(office.id, office.organization.name,
                            office.photo?.sizes?.large ?: "",
                            office.lat, office.lon, office.address, office.phone
                            ?: office.organization.phone ?: "", office.organization.email)
                }
            }

            productMapped = Product(it.id, it.organizationId, it.productCategoryId, it.name, it.description, it.price, it.oldPrice, it.totalAmount, it.soldAmount, ProductCategory(it.productCategory?.id
                    ?: it.productCategoryId, it.productCategory?.key ?: "", it.productCategory?.name
                    ?: ""), organization)
        }


        return Voucher(isProduct, isUsed, voucher.address
                ?: "", name, organizationName, voucher.fund?.name
                ?: "", description, createdAt!!, euro, amount,
                productLogoUrl, transactions, productMapped)

    }

    private fun mapToProvider(voucher: io.forus.me.android.data.entity.vouchers.response.Voucher): VoucherProvider {
        val item = mapToSimple(voucher)
        val categories = if (voucher.allowedProductCategories == null) emptyList()
        else voucher.allowedProductCategories.map { ProductCategory(it.id, it.key, it.name) }
        val organizations = if (voucher.allowedOrganizations == null) emptyList()
        else voucher.allowedOrganizations.map {
            Organization(it.id, it.name ?: "", it.logo?.sizes?.large
                    ?: "", it.lat, it.lon, it.identityAddress ?: "", it.phone ?: "", it.email ?: "")
        }
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

    override fun makeTransaction(address: String, amount: BigDecimal, note: String, organizationId: Long): Observable<Boolean> {
        return vouchersDataSource.makeTransaction(address, MakeTransaction(amount, note, organizationId)).map { true }
    }

    override fun sendEmail(address: String): Observable<Boolean> {
        return vouchersDataSource.sendEmail(address)
    }
}