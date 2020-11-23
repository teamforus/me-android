package io.forus.me.android.presentation.view.screens.vouchers.provider

import android.util.Log
import io.forus.me.android.domain.models.vouchers.Product
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.presentation.models.vouchers.*
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ProviderPresenter constructor(private val vouchersRepository: VouchersRepository, private val address: String,
                                    private val isDemoVoucher: Boolean? = false) : LRPresenter<VoucherProvider, ProviderModel, ProviderView>() {

    private var organizationId = 0L
    private var note = ""

    override fun initialModelSingle(): Single<VoucherProvider> {

        if (isDemoVoucher != null && isDemoVoucher) {

            val officesList = mutableListOf<Office>()

            return return Single.fromObservable(vouchersRepository.getTestVoucherAsProvider().map {
                VoucherProvider(Voucher(it.voucher?.isProduct ?: false, it.voucher?.isUsed
                        ?: false, it.voucher.address, it.voucher.name, it.voucher.organizationName,
                        it.voucher.fundName, it.voucher.fundType, it.voucher.fundWebShopUrl, it.voucher.description, it.voucher.createdAt,
                        Currency(it.voucher.currency?.name, it.voucher.currency?.logoUrl), it.voucher.amount, it.voucher.logo,
                        it.voucher.transactions.map { transaction ->
                            Transaction(transaction.id,
                                    Organization(transaction.organization?.id ?: 0,
                                            transaction.organization?.name,
                                            transaction.organization?.logo,
                                            transaction.organization?.lat,
                                            transaction.organization?.lon,
                                            transaction.organization?.address,
                                            transaction.organization?.phone,
                                            transaction.organization?.email),
                                    Currency(transaction.currency?.name,
                                            transaction.currency?.logoUrl),
                                    transaction?.amount ?: 0f.toBigDecimal(),
                                    transaction.createdAt,
                                    Transaction.Type.valueOf(transaction.type.name), null)
                        },  null, it.voucher.expired ?: false,"", officesList),

                        it.allowedOrganizations.map { organization ->
                            Organization(organization.id,
                                    organization.name,
                                    organization.logo,
                                    organization.lat,
                                    organization.lon,
                                    organization.address,
                                    organization.phone,
                                    organization.email)
                        },

                        it.allowedProductCategories.map { productCategory ->
                            ProductCategory(productCategory.id, productCategory.key, productCategory.name)
                        })
            })

        } else {
            return Single.fromObservable(vouchersRepository.getVoucherAsProvider(address).map {

                val officesList = mutableListOf<Office>()
                val officesMapped = it.voucher.offices.map {
                    val organization = Organization(it.organization?.id ?: 0,
                            it.organization?.name ?: "", it.organization?.logo ?: "",
                            it.organization?.lat ?: 0.0, it.organization?.lon ?: 0.0,
                            it.organization?.address ?: "",
                            it.organization?.phone ?: "",
                            it.organization?.email ?: "")

                    officesList.add(Office(it.id,it.organizationId,it.address,it.phone,it.lat,it.lon,it.photo,organization))

                }



                VoucherProvider(Voucher(it.voucher?.isProduct ?: false, it.voucher?.isUsed
                        ?: false, it.voucher.address, it.voucher.name, it.voucher.organizationName,
                        it.voucher.fundName, it.voucher.fundType, it.voucher.fundWebShopUrl, it.voucher.description, it.voucher.createdAt,
                        Currency(it.voucher.currency?.name, it.voucher.currency?.logoUrl), it.voucher.amount, it.voucher.logo,
                        it.voucher.transactions.map { transaction ->

                            val organization =  Organization(transaction.organization?.id ?: 0,
                                    transaction.organization?.name,
                                    transaction.organization?.logo,
                                    transaction.organization?.lat,
                                    transaction.organization?.lon,
                                    transaction.organization?.address,
                                    transaction.organization?.phone,
                                    transaction.organization?.email)

                            val product:io.forus.me.android.presentation.models.vouchers.Product? = if(transaction.product == null){ null }else {
                                io.forus.me.android.presentation.models.vouchers.Product(transaction.product?.id?:-1L,
                                        transaction.product!!.organizationId!!,transaction.product!!.productCategoryId!!,
                                        transaction.product!!.name,transaction.product!!.description,
                                        transaction.product!!.price!!,transaction.product!!.oldPrice!!,
                                        transaction.product!!.totalAmount!!,transaction.product!!.soldAmount!!,
                                        ProductCategory(transaction.product!!.productCategory?.id?:-1L,
                                                transaction.product!!.productCategory?.key,
                                                transaction.product!!.productCategory?.name), organization)
                            }



                            Transaction(transaction.id,organization,
                                    Currency(transaction.currency?.name,
                                            transaction.currency?.logoUrl),
                                    transaction?.amount ?: 0f.toBigDecimal(),
                                    transaction.createdAt,
                                    Transaction.Type.valueOf(transaction.type.name), product)
                        }, null, it.voucher.expired ?: false,"", officesList),

                        it.allowedOrganizations.map { organization ->
                            Organization(organization.id,
                                    organization.name,
                                    organization.logo,
                                    organization.lat,
                                    organization.lon,
                                    organization.address,
                                    organization.phone,
                                    organization.email)
                        },

                        it.allowedProductCategories.map { productCategory ->
                            ProductCategory(productCategory.id, productCategory.key, productCategory.name)
                        })
            })
        }
    }

    override fun ProviderModel.changeInitialModel(i: VoucherProvider): ProviderModel {
        val organization = if (i.allowedOrganizations.isNotEmpty()) i.allowedOrganizations.get(0) else Organization(organizationId, i.voucher.organizationName, "", i.voucher.product?.organization?.lat
                ?: 0.0, i.voucher.product?.organization?.lon
                ?: 0.0, i.voucher.product?.organization?.address
                ?: "", i.voucher.product?.organization?.phone
                ?: "", i.voucher.product?.organization?.email ?: "")
        organizationId = organization.id
        return copy(item = i, selectedOrganization = organization)
    }

    override fun bindIntents() {

        val observable = Observable.merge(

                Arrays.asList(

                        loadRefreshPartialChanges(),

                        intent { it.selectAmount() }
                                .map { ProviderPartialChanges.SetAmount(it) },

                        intent { it.selectNote() }
                                .map { ProviderPartialChanges.SetNote(it) },


                        intent { it.charge() }
                                .switchMap {
                                    vouchersRepository.makeTransaction(address, it, note, organizationId)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map<PartialChange> {
                                                ProviderPartialChanges.MakeTransactionEnd()
                                            }
                                            .onErrorReturn {
                                                ProviderPartialChanges.MakeTransactionError(it)
                                            }
                                            .startWith(ProviderPartialChanges.MakeTransactionStart())
                                },


                        intent { it.selectOrganization() }
                                .map { ProviderPartialChanges.SelectOrganization(it) },

                        intent { it.demoCharge() }
                                .switchMap {


                                    vouchersRepository.makeDemoTransaction(address)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .map<PartialChange> {
                                                ProviderPartialChanges.MakeTransactionEnd()
                                            }
                                            .onErrorReturn {
                                                ProviderPartialChanges.MakeTransactionError(it)
                                            }
                                            .startWith(ProviderPartialChanges.MakeTransactionStart())


                                }
                )
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                ProviderModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                ProviderView::render)
    }

    override fun stateReducer(vs: LRViewState<ProviderModel>, change: PartialChange): LRViewState<ProviderModel> {

        if (change !is ProviderPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is ProviderPartialChanges.MakeTransactionEnd -> vs.copy(closeScreen = true, model = vs.model.copy(sendingMakeTransaction = false, makeTransactionError = null), loading = false)
            is ProviderPartialChanges.MakeTransactionStart -> vs.copy(model = vs.model.copy(sendingMakeTransaction = true, makeTransactionError = null), loading = true)
            is ProviderPartialChanges.MakeTransactionError -> vs.copy(model = vs.model.copy(sendingMakeTransaction = false, makeTransactionError = change.error))
            is ProviderPartialChanges.SetAmount -> vs.copy(model = vs.model.copy(selectedAmount = change.amount, makeTransactionError = null))
            is ProviderPartialChanges.SetNote -> {
                note = change.note
                vs.copy(model = vs.model.copy(selectedNote = change.note, makeTransactionError = null))
            }
            is ProviderPartialChanges.SelectOrganization -> {

                organizationId = change.organization.id
                vs.copy(model = vs.model.copy(selectedOrganization = change.organization, makeTransactionError = null))
            }
        }
    }
}