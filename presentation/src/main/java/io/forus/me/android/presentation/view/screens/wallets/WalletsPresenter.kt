package io.forus.me.android.presentation.view.screens.wallets

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.domain.repository.wallets.WalletsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class WalletsPresenter constructor(val walletsRepository: WalletsRepository) : LRPresenter<List<Wallet>, WalletsModel, WalletsView>() {


    override fun initialModelSingle(): Single<List<Wallet>> = Single.fromObservable(walletsRepository.getWallets())
            //.delay(1, TimeUnit.SECONDS)
            .map {
                it
            }


    override fun WalletsModel.changeInitialModel(i: List<Wallet>): WalletsModel = copy(items = i)


    override fun bindIntents() {

//        var observable = Observable.merge(
//
//                loadRefreshPartialChanges()
//        );

        var observable = loadRefreshPartialChanges();


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                WalletsModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                WalletsView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<WalletsModel>, change: PartialChange): LRViewState<WalletsModel> {

        if (change !is WalletsPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}