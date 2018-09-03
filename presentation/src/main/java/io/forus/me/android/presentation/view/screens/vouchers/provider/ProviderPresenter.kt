package io.forus.me.android.presentation.view.screens.vouchers.provider

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.vouchers.VoucherProvider
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProviderPresenter constructor(private val vouchersRepository: VouchersRepository, private val address: String) : LRPresenter<VoucherProvider, ProviderModel, ProviderView>() {

    private var amount = 0.0f
    private var organizationId = 0L

    override fun initialModelSingle(): Single<VoucherProvider> = Single.fromObservable(vouchersRepository.getVoucherAsProvider(address))

    override fun ProviderModel.changeInitialModel(i: VoucherProvider): ProviderModel {
        val organization = if(i.allowedOrganizations.isNotEmpty()) i.allowedOrganizations.get(0) else null
        if(organization != null) organizationId = organization.id
        return copy(item = i, selectedOrganization = organization)
    }

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.selectAmount() }
                        .map {  amount = it; ProviderPartialChanges.SetAmount(it) },

                intent { it.submit() }
                        .switchMap {
                            vouchersRepository.makeTransaction(address, amount, organizationId)
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
                ProviderModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                ProviderView::render)
    }

    override fun stateReducer(vs: LRViewState<ProviderModel>, change: PartialChange): LRViewState<ProviderModel> {

        if (change !is ProviderPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is ProviderPartialChanges.MakeTransactionEnd -> vs.copy(closeScreen = true, model = vs.model.copy(sendingMakeTransaction = false, makeTransactionError = null))
            is ProviderPartialChanges.MakeTransactionStart -> vs.copy(model = vs.model.copy(sendingMakeTransaction = true, makeTransactionError = null))
            is ProviderPartialChanges.MakeTransactionError -> vs.copy(model = vs.model.copy(sendingMakeTransaction = false, makeTransactionError = change.error))
            is ProviderPartialChanges.SetAmount -> vs.copy(model = vs.model.copy(selectedAmount = change.amount))
        }
    }
}