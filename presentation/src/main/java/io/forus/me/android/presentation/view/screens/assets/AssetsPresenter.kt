package io.forus.me.android.presentation.view.screens.assets

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.domain.repository.assets.AssetsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class AssetsPresenter constructor(val assetsRepository: AssetsRepository) : LRPresenter<List<Asset>, AssetsModel, AssetsView>() {


    override fun initialModelSingle(): Single<List<Asset>> = Single.fromObservable(assetsRepository.getAssets())
            //.delay(1, TimeUnit.SECONDS)
            .map {
                it
            }


    override fun AssetsModel.changeInitialModel(i: List<Asset>): AssetsModel = copy(items = i)


    override fun bindIntents() {

//        var observable = Observable.merge(
//
//                loadRefreshPartialChanges()
//        );

        var observable = loadRefreshPartialChanges()


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                AssetsModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                AssetsView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<AssetsModel>, change: PartialChange): LRViewState<AssetsModel> {

        if (change !is AssetsPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}