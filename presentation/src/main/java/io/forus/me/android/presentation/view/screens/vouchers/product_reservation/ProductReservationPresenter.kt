package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.firestore_logging.FirestoreTokenManager
import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.presentation.models.vouchers.*
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange

import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersPartialChanges
import io.forus.me.android.presentation.view.screens.vouchers.list.VouchersView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class ProductReservationPresenter constructor(val vouchersRepository: VouchersRepository,
                                              val voucherAddress: String,
                                              private val firestoreTokenManager: FirestoreTokenManager)
    : LRPresenter<List<Voucher>, ProductReservationModel, ProductReservationView>() {


    override fun initialModelSingle(): Single<List<Voucher>> = Single.fromObservable(vouchersRepository.getProductVouchersAsProvider(voucherAddress).map { domainVouchers ->
        domainVouchers.map { domainVoucher ->
            with(domainVoucher) {

                firestoreTokenManager.writeGetProductVoucherAsProvider(
                    voucherAddress, true, null
                )

                val transactionsMapped = transactions.map {
                    val organization = Organization(it.organization?.id ?: 0,
                            it.organization?.name ?: "", it.organization?.logo ?: "",
                            it.organization?.lat ?: 0.0, it.organization?.lon ?: 0.0,
                            it.organization?.address ?: "",
                            it.organization?.phone ?: "",
                            it.organization?.email ?: "")

                    val product:io.forus.me.android.presentation.models.vouchers.Product? =
                            if(it.product == null){ null }else{
                                Product(it.product!!.id?:-1L, it.product!!.organizationId?:-1L,
                                        it.product!!.productCategoryId?:-1L,
                                        it.product!!.name,it.product!!.description,
                                        it.product!!.price?: BigDecimal(-1),
                                        it.product!!.oldPrice?: BigDecimal(-1), it.product!!.totalAmount?:-1L,
                                        it.product!!.soldAmount?:-1L,
                                        ProductCategory(it.product!!.productCategory?.id?:-1L,
                                                it.product!!.productCategory?.key,
                                                it.product!!.productCategory?.name),organization)
                            }


                    Transaction(it.id, organization,
                            Currency(it.currency?.name ?: "", it.currency?.logoUrl ?: ""),
                            it?.amount ?: 0f.toBigDecimal(), it.createdAt,
                            Transaction.Type.valueOf(it.type.name),
                    product)
                }

                val domainProduct = domainVoucher.product
                val product = when (domainProduct) {
                    null -> null
                    else -> Product(domainProduct.id?:-1L,
                            domainProduct.organizationId?:-1L,
                            domainProduct.productCategoryId?:-1L,
                            domainProduct.name, domainProduct.description,
                            domainProduct.price?: BigDecimal(-1), domainProduct.oldPrice?: BigDecimal(-1),
                            domainProduct.totalAmount?:-1L, domainProduct.soldAmount?:-1L,
                            ProductCategory(    domainProduct.productCategory?.id?:-1L,
                                    domainProduct.productCategory?.key, domainProduct.productCategory?.name),
                            Organization(domainProduct.organization?.id?:-1L,
                                    domainProduct.organization?.name, domainProduct.organization?.logo, domainProduct.organization?.lat,
                                    domainProduct.organization?.lon, domainProduct.organization?.address, domainProduct.organization?.phone, domainProduct.organization?.email))
                }

                val officesList = mutableListOf<Office>()
                val officesMapped = offices.map {

                    val schedulers = mutableListOf<io.forus.me.android.presentation.models.vouchers.Schedule>()
                    if(it.schedulers != null){
                        it.schedulers!!.map {
                            schedulers.add(io.forus.me.android.presentation.models.vouchers.Schedule(it.id,it.officeId ?:-1L,it.weekDay?:0,it.startTime,it.endTime))
                        }
                    }

                    val organization = Organization(it.organization?.id ?: 0,
                            it.organization?.name ?: "", it.organization?.logo ?: "",
                            it.organization?.lat ?: 0.0, it.organization?.lon ?: 0.0,
                            it.organization?.address ?: "",
                            it.organization?.phone ?: "",
                            it.organization?.email ?: "")

                    officesList.add(Office(it.id,it.organizationId,it.address,it.phone,it.lat,it.lon,it.photo,organization,schedulers))

                }

                Voucher(isProduct ?: false, isUsed ?: false, address, identyAddress, name, organizationName,
                        fundName, fundType, fundWebShopUrl, description, createdAt, Currency(currency?.name, currency?.logoUrl), amount, logo,
                        transactionsMapped, product, deactivated ?: false, expired ?: false, expireDate,officesList
                )
            }
        }
    })

    override fun ProductReservationModel.changeInitialModel(i: List<Voucher>): ProductReservationModel {
        val vouchers: MutableList<Voucher> = mutableListOf()
        vouchers.addAll(i.filter { voucher -> !(voucher.isProduct && voucher.isUsed) })
        //vouchers.addAll(i.filter { voucher ->  (voucher.isProduct && voucher.isUsed)})
        return copy(items = vouchers)
    }


    override fun bindIntents() {
        val observable = loadRefreshPartialChanges()


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                ProductReservationModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                ProductReservationView::render)

    }

    /*private fun subscribeViewState(observeOn: Observable<LRViewState<VouchersModel>>?, kFunction2: KFunction2<ProductReservationView, LRViewState<ProductReservationModel>, Unit>) {

    }*/

    override fun stateReducer(viewState: LRViewState<ProductReservationModel>, change: PartialChange): LRViewState<ProductReservationModel> {

        if (change !is VouchersPartialChanges) return super.stateReducer(viewState, change)

        return run {
            super.stateReducer(viewState, change)
        }

    }


}