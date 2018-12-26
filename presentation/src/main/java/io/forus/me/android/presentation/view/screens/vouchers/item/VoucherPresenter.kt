package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.models.vouchers.Transaction
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class VoucherPresenter constructor(private val vouchersRepository: VouchersRepository,
                                   private val address: String,
                                   private val voucher: Voucher? = null) : LRPresenter<Voucher, VoucherModel, VoucherView>() {


    override fun initialModelSingle(): Single<Voucher> {
        val voucherObservable = if (voucher != null) Single.just(voucher) else null

        return voucherObservable ?: Single.fromObservable(
                vouchersRepository.getVoucher(address).map { domainVoucher ->
                    with(domainVoucher) {
                        Voucher(isProduct, isUsed, address, name, organizationName,
                                fundName, fundWebShopUrl, description, createdAt,
                                Currency(currency.name, currency.logoUrl), amount, logo,
                                transactions.map {
                                    Transaction(it.id, Organization(it.organization.id,
                                            it.organization.name, it.organization.logo),
                                            Currency(it.currency.name, it.currency.logoUrl),
                                            it.amount, createdAt,
                                            Transaction.Type.valueOf(it.type.name))
                                })
                    }

                })
    }


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