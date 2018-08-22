package io.forus.me.android.presentation.view.screens.account.assigndelegates

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class AssignDeligatesPresenter constructor(val accountRepository: AccountRepository) : LRPresenter<RequestDelegatesQrModel, AssignDelegatesModel, AssignDelegatesView>() {


    override fun initialModelSingle(): Single<RequestDelegatesQrModel> = Single.fromObservable(accountRepository.restoreByQrToken())
            //.delay(1, TimeUnit.SECONDS)


    override fun AssignDelegatesModel.changeInitialModel(i: RequestDelegatesQrModel): AssignDelegatesModel = copy(item = i)


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
                AssignDelegatesModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                AssignDelegatesView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<AssignDelegatesModel>, change: PartialChange): LRViewState<AssignDelegatesModel> {

        if (change !is AssignDelegatesPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}