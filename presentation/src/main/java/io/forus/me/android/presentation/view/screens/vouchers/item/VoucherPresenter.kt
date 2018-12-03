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


    override fun initialModelSingle(): Single<Voucher> =
            Single.fromObservable(vouchersRepository.getVoucher(address))

    override fun VoucherModel.changeInitialModel(i: Voucher): VoucherModel = copy(item = i)

    override fun attachView(view: VoucherView) {
        super.attachView(view)


    }

    override fun bindIntents() {
        val infoObservable = Observable.merge(
                loadRefreshPartialChanges(),
                intent {
                    it.showInfo()
                }
        )


        val emailObservable = Observable.merge(
                loadRefreshPartialChanges(),

                intent(VoucherView::sendEmail).map {
                    VoucherPartialChanges.SendEmailDialogShows(Unit)
                },

                intent(VoucherView::sendEmailDialogShows).switchMap {
                    if (it) {
                         vouchersRepository.sendEmail(address)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map<VoucherPartialChanges> {
                                    VoucherPartialChanges.SendEmailSuccess(Unit)
                                }
                                .onErrorReturn { error ->
                                    VoucherPartialChanges.SendEmailError(error)
                                }
                    } else {
                         Observable.just(VoucherPartialChanges.SendEmailDialogShown(Unit))
                    }
                },

                intent(VoucherView::sentEmailDialogShown).map {
                    VoucherPartialChanges.SendEmailDialogShown(Unit)
                }

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
                emailObservable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                VoucherView::render)
    }

    override fun stateReducer(vs: LRViewState<VoucherModel>, change: PartialChange): LRViewState<VoucherModel> {

        if (change !is VoucherPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is VoucherPartialChanges.SendEmailSuccess -> vs.copy(model = vs.model.copy(emailSend = EmailSend.SENT))
            is VoucherPartialChanges.SendEmailError -> vs.copy(loadingError = change.error)
            is VoucherPartialChanges.SendEmailDialogShown -> vs.copy(model = vs.model.copy(emailSend = EmailSend.NOTHING))
            is VoucherPartialChanges.SendEmailDialogShows -> vs.copy(model = vs.model.copy(emailSend = EmailSend.SEND))
        }
    }
}