package io.forus.me.android.presentation.view.screens.vouchers.provider

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

class ProviderPresenter constructor(private val vouchersRepository: VouchersRepository, private val address: String) : LRPresenter<VoucherProvider, ProviderModel, ProviderView>() {

    private var organizationId = 0L
    private var note = ""

    override fun initialModelSingle(): Single<VoucherProvider> = Single.fromObservable(vouchersRepository.getVoucherAsProvider(address).map {
        VoucherProvider(Voucher(it.voucher?.isProduct ?: false, it.voucher?.isUsed ?: false, it.voucher.address, it.voucher.name, it.voucher.organizationName,
                it.voucher.fundName, it.voucher.fundWebShopUrl, it.voucher.description, it.voucher.createdAt,
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
                            Transaction.Type.valueOf(transaction.type.name))
                }),

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
                        }

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
            is ProviderPartialChanges.MakeTransactionEnd -> vs.copy(closeScreen = true, model = vs.model.copy(sendingMakeTransaction = false, makeTransactionError = null), loading=false)
            is ProviderPartialChanges.MakeTransactionStart -> vs.copy(model = vs.model.copy(sendingMakeTransaction = true, makeTransactionError = null), loading=true)
            is ProviderPartialChanges.MakeTransactionError -> vs.copy(model = vs.model.copy(sendingMakeTransaction = false, makeTransactionError = change.error))
            is ProviderPartialChanges.SetAmount -> vs.copy(model = vs.model.copy(selectedAmount = change.amount, makeTransactionError = null))
            is ProviderPartialChanges.SetNote -> {
                note = change.note
                vs.copy(model = vs.model.copy(selectedNote = change.note, makeTransactionError = null))
            }

        }
    }
}