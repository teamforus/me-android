package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class VoucherPresenter constructor(private val vouchersRepository: VouchersRepository, private val address: String) : LRPresenter<Voucher, VoucherModel, VoucherView>() {


    override fun initialModelSingle(): Single<Voucher> = Single.fromObservable(vouchersRepository.getVoucher(address))

    override fun VoucherModel.changeInitialModel(i: Voucher): VoucherModel = copy(item = i)

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.sendEmail() }
                        .switchMap {
                            vouchersRepository.sendEmail(address)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map <VoucherPartialChanges> {
                                        VoucherPartialChanges.SendEmailSuccess(Unit)
                                    }
                                    .onErrorReturn {
                                        VoucherPartialChanges.SendEmailError(it)
                                    }
                        },

                intent { it.sendEmailDialogShown()}
                        .map { VoucherPartialChanges.SendEmailDialogShown(Unit) }

        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                VoucherModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                VoucherView::render)
    }

    override fun stateReducer(vs: LRViewState<VoucherModel>, change: PartialChange): LRViewState<VoucherModel> {

        if (change !is VoucherPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is VoucherPartialChanges.SendEmailSuccess -> vs.copy(model = vs.model.copy(emailSend = true))
            is VoucherPartialChanges.SendEmailError -> vs.copy(loadingError = change.error)
            is VoucherPartialChanges.SendEmailDialogShown -> vs.copy(model = vs.model.copy(emailSend = false))
        }
    }
}