package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.domain.interactor.LoadVoucherUseCase
import io.forus.me.android.domain.interactor.SendEmailUseCase
import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.presentation.models.vouchers.*
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.ReplaySubject
import io.forus.me.android.domain.models.vouchers.Voucher as VoucherDomain


class VoucherPresenter constructor(private val loadVoucherUseCase: LoadVoucherUseCase,
                                   private val sendEmailUseCase: SendEmailUseCase,
                                   private val address: String,
                                   private var voucher: Voucher? = null) : LRPresenter<Voucher, VoucherModel, VoucherView>() {

    private val voucherSubject: ReplaySubject<Voucher> by lazy { ReplaySubject.create<Voucher>() }

    override fun initialModelSingle(): Single<Voucher> {
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
        loadVoucherUseCase.execute(LoadVoucherObserver(), LoadVoucherUseCase.Params.forVoucher(address))
        return voucherSubject.singleOrError()
    }
  
    override fun VoucherModel.changeInitialModel(i: Voucher): VoucherModel = copy(item = i)

    override fun bindIntents() {

        val observable = Observable.merge(
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
        }
    }

    inner class LoadVoucherObserver : DisposableObserver<VoucherDomain?>() {
        override fun onComplete() {
        }

        override fun onNext(voucherDomain: VoucherDomain) {
            val productDomain = voucherDomain.product
            val product = if(productDomain != null) {
                Product(productDomain.id,
                        productDomain.organizationId,
                        productDomain.productCategoryId,
                        productDomain.name,
                        productDomain.description,
                        productDomain.price,
                        productDomain.oldPrice,
                        productDomain.totalAmount,
                        productDomain.soldAmount,
                        ProductCategory(productDomain.productCategory.id,
                                productDomain.productCategory.key,
                                productDomain.productCategory.name),
                        Organization(productDomain.organization.id,
                                productDomain.organization.name,
                                productDomain.organization.logo,
                                productDomain.organization.lat,
                                productDomain.organization.lon,
                                productDomain.organization.address,
                                productDomain.organization.phone,
                                productDomain.organization.email))
            } else {
                null
            }

            val voucherNext = Voucher(voucherDomain.isProduct,
                    voucherDomain.isUsed,
                    voucherDomain.address,
                    voucherDomain.name,
                    voucherDomain.organizationName,
                    voucherDomain.fundName,
                    voucherDomain.description,
                    voucherDomain.createdAt,
                    Currency(voucherDomain.currency.name,
                            voucherDomain.currency.logoUrl),
                    voucherDomain.amount,
                    voucherDomain.logo,

                    voucherDomain.transactions.map {
                        Transaction(it.id, Organization(it.organization.id,
                                it.organization.name,
                                it.organization.logo),
                                Currency(it.currency.name,
                                        it.currency.logoUrl),
                                it.amount,
                                it.createdAt,
                                Transaction.Type.valueOf(it.type.name))
                    }, product)
            voucherSubject.onNext(voucherNext)
            voucherSubject.onComplete()
        }

        override fun onError(e: Throwable) {
        }
    }

}