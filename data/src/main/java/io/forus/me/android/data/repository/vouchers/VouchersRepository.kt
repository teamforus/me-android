package io.forus.me.android.data.repository.vouchers

import android.util.Log
import io.forus.me.android.data.entity.vouchers.request.MakeActionTransaction
import io.forus.me.android.data.entity.vouchers.request.MakeDemoTransaction
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

                val fund = Fund(it.fund.id, it.fund.name, it.fund.organization?.id, null,
                        Organization(it.organization.id, it.organization.name, null, null, null, null, null, null))

                val product = if (it.product == null) {
                    null
                } else {
                    Product(it.product.id, it.product.organizationId,
                            it.product.productCategoryId,
                            it.product.name, it.product.description,
                            it.product.price, it.product.oldPrice,
                            it.product.totalAmount, it.product.soldAmount,
                            ProductCategory(it.product.productCategory.id,
                                    it.product.productCategory.key,
                                    it.product.productCategory.name),
                            Organization(it.product.organization.id, it.product.organization.name,
                                    it.product.organization.logo.sizes.thumbnail, it.product.organization.lat,
                                    it.product.organization.lon, it.product.organization.identityAddress,
                                    it.product.organization.phone, it.product.organization.email))
                }

                Transaction(it.address, Organization(it.organization.id, it.organization.name, it.organization?.logo?.sizes?.large
                        ?: "", it.organization?.lat ?: 0.0, it.organization?.lon
                        ?: 0.0, it.organization?.identityAddress ?: "", it.organization?.phone
                        ?: "", it.organization?.email
                        ?: ""), euro, it.amount, it.createdAt, product, it.state,
                        fund)
            })
        }

        if (voucher.childVouchers != null) {
            transactions.addAll(voucher.childVouchers.map { childVoucher ->


                val organization = childVoucher.product?.organization
                val product = if (voucher.product == null) {
                    null
                } else {
                    Product(voucher.product.id, voucher.product.organizationId,
                            voucher.product.productCategoryId,
                            voucher.product.name, voucher.product.description,
                            voucher.product.price, voucher.product.oldPrice,
                            voucher.product.totalAmount, voucher.product.soldAmount,
                            ProductCategory(voucher.product.productCategory.id,
                                    voucher.product.productCategory.key,
                                    voucher.product.productCategory.name),
                            Organization(voucher.product.organization.id, voucher.product.organization.name,
                                    voucher.product.organization.logo.sizes.thumbnail, voucher.product.organization.lat,
                                    voucher.product.organization.lon, voucher.product.organization.identityAddress,
                                    voucher.product.organization.phone, voucher.product.organization.email))
                }
                Transaction("", Organization(childVoucher.product.organizationId, childVoucher.product.name, "", organization?.lat
                        ?: 0.0, organization?.lon ?: 0.0, organization?.identityAddress
                        ?: "", organization?.phone ?: "", organization?.email
                        ?: ""), euro, childVoucher.amount, childVoucher.createdAt, product, null, null)
            })
        }
        transactions.sortWith(Comparator { t1, t2 ->
            -(t1.createdAt?.compareTo(t2.createdAt) ?: 0)
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




        val offices = mutableListOf<io.forus.me.android.domain.models.vouchers.Office>()
        if (voucher.offices != null) {
            voucher.offices.map {
                var organization = Organization(it.organization.id,
                        it.organization.name, organizationLogoUrl, it.organization.lat,
                        it.organization.lon, it.organization.identityAddress ?: "",
                        it.organization.phone ?: "", it.organization.email ?: "")
                offices.add(Office(it.id,it.organizationId,it.address,it.phone,it.lat,it.lon,it.phone,organization))
            }
        }

        return Voucher(isProduct, isUsed, voucher.address
                ?: "", name, organizationName, voucher.fund?.name
                ?: "", voucher.fund?.type
                ?: "", voucher.fund?.webShopUrl ?: "", description, createdAt!!, euro, amount,
                productLogoUrl, transactions, productMapped, voucher.isExpired, voucher.expireAtLocale
                ?: "" , offices)

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

    private fun mapToProductAction(productAction: io.forus.me.android.data.entity.vouchers.response.ProductAction): ProductAction {

        val organization: Organization? = if (productAction.organization == null) {
            null
        } else {
            val org = productAction.organization
            Organization(org.id, org.name, null, org.lat, org.lon, org.identityAddress, org.phone, org.email)
        }

        val productCategory: ProductCategory? = if (productAction.productCategory == null) {
            null
        } else {
            val prc = productAction.productCategory
            ProductCategory(prc.id, prc.key, prc.name)
        }

        val photoUrl: String? = if (productAction.photo != null && productAction.photo.sizes != null && productAction.photo.sizes.large != null) {
            productAction.photo.sizes.large
        } else {
            null
        }


        return ProductAction(productAction.id, productAction.name, productAction.organizationId, productAction.price, productAction.priceUser,
                photoUrl, organization, productCategory)
    }

    private fun mapToLogTransaction(transaction: io.forus.me.android.data.entity.vouchers.response.Transaction): Transaction {


        val fund = Fund(transaction.fund.id, transaction.fund.name, transaction.fund.organization?.id, null,
                Organization(transaction.organization.id, transaction.organization.name, null, null, null, null, null, null))


        val organization: Organization? = if (transaction.organization == null) {
            null
        } else {
            val org = transaction.organization
            Organization(org.id, org.name, null, org.lat, org.lon, org.identityAddress, org.phone, org.email)
        }


        val product: Product? = if (transaction.product == null) {
            null
        } else {
            val prc = transaction.product
            Product(prc.id, prc.organizationId, null, prc.name, null, prc.price,
                    null, null, null, null, null)
        }




        return Transaction(transaction.id.toString(), organization, null, transaction.amount,
                transaction.createdAt, product, transaction.state, fund)

    }


    private fun getFakeVoucherProvider(): VoucherProvider {
        val date = Calendar.getInstance().getTime()
        val currency = Currency("EUR", "")

        val organization = Organization(0, "Test bedrijf", "", 0.0, 0.0,
                "", "", "")
        val organizationsList = mutableListOf<Organization>()
        organizationsList.add(organization)
        val transaction = Transaction("0", organization, currency, 0f.toBigDecimal(), date, null,
                null, null)
        val transactionList = mutableListOf<Transaction>()
        transactionList.add(transaction)

        val productCategory = ProductCategory(0, "", "")
        val productCategoryList = mutableListOf<ProductCategory>()
        productCategoryList.add(productCategory)
        val product = Product(0, 0, 0, "", "",
                0f.toBigDecimal(), 0f.toBigDecimal(),
                0, 0, productCategory, organization)

        val offices = mutableListOf<io.forus.me.android.domain.models.vouchers.Office>()

        val voucher = Voucher(false, false, "", "Test bedrijf",
                "", "", "", "",
                "", date, currency, 1000.toBigDecimal(), "",
                transactionList, product, false, "",offices)

        return VoucherProvider(voucher, organizationsList, productCategoryList)
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

    override fun getTestVoucherAsProvider(): Observable<VoucherProvider> {

        return Observable.just(getFakeVoucherProvider())
    }

    override fun getProductVouchersAsProvider(address: String): Observable<List<Voucher>> {
        return vouchersDataSource.retrieveProductVouchersAsProvider(address).map { it.map { mapToSimple(it) } }
    }

    override fun getVoucherProductsActionsAsProvider(address: String, organizationId: Long, page: Int, perPage: Int): Observable<List<ProductAction>> {
        return vouchersDataSource.retrieveVoucherProductsActionsAsProvider(address, organizationId, page, perPage).map { it.map { mapToProductAction(it) } }
    }

    override fun getTransactionsLogAsProvider(from: String, page: Int, perPage: Int): Observable<List<Transaction>> {
        return vouchersDataSource.retrieveTransactionsLogAsProvider(from, page, perPage).map { it.map { mapToLogTransaction(it) } }
    }

    override fun makeTransaction(address: String, amount: BigDecimal, note: String, organizationId: Long): Observable<Boolean> {
        return vouchersDataSource.makeTransaction(address, MakeTransaction(amount, note, organizationId)).map { true }
    }

    override fun makeActionTransaction(address: String, note: String, productId: Long): Observable<Boolean> {
        return vouchersDataSource.makeActionTransaction(address, MakeActionTransaction(productId, note)).map { true }
    }

    override fun sendEmail(address: String): Observable<Boolean> {
        return vouchersDataSource.sendEmail(address)
    }

    override fun makeDemoTransaction(testToken: String): Observable<Boolean> {
        return vouchersDataSource.makeDemoTransaction(testToken, MakeDemoTransaction("accepted")).map {
            it.data.state == "accepted"
        }
    }
}