package io.forus.me.android.presentation.view.screens.wallets.item

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.wallets.Transaction
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.domain.repository.wallets.WalletsRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class WalletDetailsPresenter constructor(private val walletId: Long, private val walletsRepository: WalletsRepository) : LRPresenter<WalletDetailsModel, WalletDetailsModel, WalletDetailsView>() {


    override fun initialModelSingle(): Single<WalletDetailsModel> = Single.zip(
            Single.fromObservable(walletsRepository.getWallet(walletId)),
            Single.fromObservable(walletsRepository.getTransactions(walletId)),
            BiFunction { wallet: Wallet, transactions: List<Transaction> ->
                WalletDetailsModel(wallet, transactions)
            }
    )


    override fun WalletDetailsModel.changeInitialModel(i: WalletDetailsModel): WalletDetailsModel = copy(item = i.item, transactions = i.transactions).also {
        if(i.item?.address != null && i.item.address.isNotEmpty()) {
            loadQrCode.onNext(i.item.address)
        }
    }

    private val loadQrCode = PublishSubject.create<String>()
    private fun loadQrCode(): Observable<String> = loadQrCode

    override fun bindIntents() {

        val observable = loadRefreshPartialChanges()

//        var observable = Observable.merge(
//
//                loadRefreshPartialChanges(),
//                intent { loadQrCode() }
//                        .switchMap {
//                            QrCodeGenerator.getRecordQrCode(it, 300, 300)
//                                    .toObservable()
//                                    .subscribeOn(Schedulers.computation())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .map<PartialChange>{
//                                        WalletDetailsPartialChanges.CreateQrCodeEnd(it)
//                                    }
//                                    .onErrorReturn {
//                                        WalletDetailsPartialChanges.CreateQrCodeEnd(null)
//                                    }
//                                    .startWith(WalletDetailsPartialChanges.CreateQrCodeStart(it))
//                        }
//        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                WalletDetailsModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                WalletDetailsView::render)
    }

    override fun stateReducer(vs: LRViewState<WalletDetailsModel>, change: PartialChange): LRViewState<WalletDetailsModel> {

        if (change !is WalletDetailsPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is WalletDetailsPartialChanges.CreateQrCodeStart -> vs.copy(model = vs.model.copy(creatingQrCode = true, qrCode = null))
            is WalletDetailsPartialChanges.CreateQrCodeEnd -> vs.copy(model = vs.model.copy(creatingQrCode = false, qrCode = change.bitmap))
        }

    }
}