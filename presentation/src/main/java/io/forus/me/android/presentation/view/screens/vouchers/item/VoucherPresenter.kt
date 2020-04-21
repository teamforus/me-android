package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.domain.interactor.LoadVoucherUseCase
import io.forus.me.android.domain.interactor.SendEmailUseCase
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.mappers.VoucherDataMapper
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import java.util.*
import io.forus.me.android.domain.models.vouchers.Voucher as VoucherDomain


class VoucherPresenter constructor(private val loadVoucherUseCase: LoadVoucherUseCase,
                                   private val sendEmailUseCase: SendEmailUseCase,
                                   private val voucherDataMapper: VoucherDataMapper,
                                   private val address: String,
                                   private val voucher: Voucher? = null,
                                   private val accountRepository: AccountRepository) : LRPresenter<Voucher, VoucherModel, VoucherView>() {

    private lateinit var voucherSubject: Subject<Voucher>

    override fun initialModelSingle(): Single<Voucher> {
        voucherSubject = ReplaySubject.create<Voucher>()
        if (voucher != null) {
            voucherSubject.onNext(voucher)
            voucherSubject.onComplete()
        } else {
            loadVoucherUseCase.execute(LoadVoucherObserver(),
                    LoadVoucherUseCase.Params.forVoucher(address))
        }
        return voucherSubject.singleOrError()
    }

    override fun refreshModelSingle(): Single<Voucher> {
        voucherSubject = ReplaySubject.create<Voucher>()
        loadVoucherUseCase.execute(LoadVoucherObserver(), LoadVoucherUseCase.Params.forVoucher(address))

        return voucherSubject.singleOrError()
    }

    override fun VoucherModel.changeInitialModel(i: Voucher): VoucherModel = copy(item = i)

    override fun bindIntents() {

        val observable = Observable.merge(

                Arrays.asList(
                loadRefreshPartialChanges(),

                intent(VoucherView::sendEmail).map {
                    VoucherPartialChanges.SendEmailDialogShows(Unit)
                },

                intent(VoucherView::sendEmailDialogShows).switchMap {
                    if (it) {
                        val observable = ReplaySubject.create<VoucherPartialChanges>()
                        sendEmailUseCase.execute(object : DisposableObserver<Boolean?>() {
                            override fun onComplete() {

                            }

                            override fun onNext(t: Boolean) {
                                observable.onNext(VoucherPartialChanges.SendEmailSuccess(Unit))
                                voucherSubject.onComplete()
                            }

                            override fun onError(e: Throwable) {
                                observable.onNext(VoucherPartialChanges.SendEmailError(e))
                                voucherSubject.onComplete()
                            }
                        }, SendEmailUseCase.Params.forVoucher(address))
                        return@switchMap observable
                    } else {
                        Observable.just(VoucherPartialChanges.SendEmailDialogShown(Unit))
                    }
                },

                intent(VoucherView::sentEmailDialogShown).map {
                    VoucherPartialChanges.SendEmailDialogShown(Unit)
                },


                intent (VoucherView::getShortToken )
                        .switchMap {
                            accountRepository.getShortToken()
                                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        VoucherPartialChanges.GetShortToken(it)
                                    }

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
                VoucherModel(),
                false)


        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
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
            is VoucherPartialChanges.GetShortToken -> vs.copy(model = vs.model.copy(shortToken = change.value))
        }
    }

    inner class LoadVoucherObserver : DisposableObserver<VoucherDomain?>() {
        override fun onComplete() {
        }

        override fun onNext(voucherDomain: VoucherDomain) {
            voucherSubject.onNext(voucherDataMapper.transform(voucherDomain))
            voucherSubject.onComplete()
        }

        override fun onError(e: Throwable) {
        }
    }

}