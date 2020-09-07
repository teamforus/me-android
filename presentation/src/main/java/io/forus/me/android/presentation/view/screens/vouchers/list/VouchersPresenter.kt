package io.forus.me.android.presentation.view.screens.vouchers.list

import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.presentation.models.vouchers.*
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class VouchersPresenter constructor(val vouchersRepository: VouchersRepository) : LRPresenter<List<Voucher>, VouchersModel, VouchersView>() {


    override fun initialModelSingle(): Single<List<Voucher>> = Single.fromObservable(vouchersRepository.getVouchers().map { domainVouchers ->
        domainVouchers.map { domainVoucher ->
            with(domainVoucher) {

                val transactionsMapped = transactions.map {

                    val organization = Organization(it.organization?.id ?: 0,
                            it.organization?.name ?: "", it.organization?.logo ?: "",
                            it.organization?.lat ?: 0.0, it.organization?.lon ?: 0.0,
                            it.organization?.address ?: "",
                            it.organization?.phone ?: "",
                            it.organization?.email ?: "")

                    val product:io.forus.me.android.presentation.models.vouchers.Product? =
                            if(it.product == null){ null }else{
                                Product(it.product!!.id, it.product!!.organizationId,
                                        it.product!!.productCategoryId,
                                        it.product!!.name,it.product!!.description,
                                        it.product!!.price,
                                        it.product!!.oldPrice, it.product!!.totalAmount,
                                        it.product!!.soldAmount,
                                        ProductCategory(it.product!!.productCategory.id,
                                                it.product!!.productCategory.key,
                                                it.product!!.productCategory.name),organization)
                            }



                    Transaction(it.id, organization,
                            Currency(it.currency?.name ?: "", it.currency?.logoUrl ?: ""),
                            it?.amount ?: 0f.toBigDecimal(), it.createdAt,
                            Transaction.Type.valueOf(it.type.name), product)
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
                        fundName, fundType,fundWebShopUrl, description, createdAt, Currency(currency?.name, currency?.logoUrl), amount, logo,
                        transactionsMapped, product, expired ?: false, expireDate
                        )
            }
        }
    })

    override fun VouchersModel.changeInitialModel(i: List<Voucher>): VouchersModel {
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
                VouchersModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                VouchersView::render)

    }

    override fun stateReducer(viewState: LRViewState<VouchersModel>, change: PartialChange): LRViewState<VouchersModel> {

        if (change !is VouchersPartialChanges) return super.stateReducer(viewState, change)

        return run {
            super.stateReducer(viewState, change)
        }

    }


}