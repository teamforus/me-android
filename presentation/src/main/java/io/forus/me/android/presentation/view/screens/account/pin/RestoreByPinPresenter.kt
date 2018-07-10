package io.forus.me.android.presentation.view.screens.account.pin

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class RestoreByPinPresenter constructor(val accountRepository: AccountRepository) : LRPresenter<RequestDelegatesPinModel, RestoreByPinModel, RestoreByPinView>() {


    override fun initialModelSingle(): Single<RequestDelegatesPinModel> = Single.fromObservable(accountRepository.getLoginPin())
            //.delay(1, TimeUnit.SECONDS)


    override fun RestoreByPinModel.changeInitialModel(i: RequestDelegatesPinModel): RestoreByPinModel = copy(item = i)


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
                RestoreByPinModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RestoreByPinView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<RestoreByPinModel>, change: PartialChange): LRViewState<RestoreByPinModel> {

        if (change !is RestoreByPinPartialChanges) return super.stateReducer(viewState, change)

        return run {
            super.stateReducer(viewState, change)
        }

    }











}