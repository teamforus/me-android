package io.forus.me.android.presentation.view.screens.vouchers.product_reservation

import io.forus.me.android.domain.repository.vouchers.VouchersRepository
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

class ProductReservationPresenter constructor(val vouchersRepository: VouchersRepository, val voucherAddress: String) : LRPresenter<List<Voucher>, ProductReservationModel, ProductReservationView>() {


    override fun initialModelSingle(): Single<List<Voucher>> = Single.fromObservable(vouchersRepository.getProductVouchersAsProvider(voucherAddress).map { domainVouchers ->
        domainVouchers.map { domainVoucher ->
            with(domainVoucher) {

                val transactionsMapped = transactions.map {
                    val organization = Organization(it.organization?.id ?: 0,
                            it.organization?.name ?: "", it.organization?.logo ?: "",
                            it.organization?.lat ?: 0.0, it.organization?.lon ?: 0.0,
                            it.organization?.address ?: "",
                            it.organization?.phone ?: "",
                            it.organization?.email ?: "")

                    Transaction(it.id, organization,
                            Currency(it.currency?.name ?: "", it.currency?.logoUrl ?: ""),
                            it?.amount ?: 0f.toBigDecimal(), it.createdAt, Transaction.Type.valueOf(it.type.name))
                }

                val domainProduct = domainVoucher.product
                val product = when (domainProduct) {
                    null -> null
                    else -> Product(domainProduct.id,
                            domainProduct.organizationId,
                            domainProduct.productCategoryId,
                            domainProduct.name, domainProduct.description,
                            domainProduct.price, domainProduct.oldPrice,
                            domainProduct.totalAmount, domainProduct.soldAmount,
                            ProductCategory(    domainProduct.productCategory.id,
                                    domainProduct.productCategory.key, domainProduct.productCategory.name),
                            Organization(domainProduct.organization.id,
                                    domainProduct.organization.name, domainProduct.organization.logo, domainProduct.organization.lat, domainProduct.organization.lon, domainProduct.organization.address, domainProduct.organization.phone, domainProduct.organization.email))
                }

                Voucher(isProduct ?: false, isUsed ?: false, address, name, organizationName,
                        fundName, fundWebShopUrl, description, createdAt, Currency(currency?.name, currency?.logoUrl), amount, logo,
                        transactionsMapped, product
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