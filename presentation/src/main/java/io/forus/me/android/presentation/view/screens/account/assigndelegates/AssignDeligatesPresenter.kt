package io.forus.me.android.presentation.view.screens.account.assigndelegates

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class AssignDeligatesPresenter constructor() : LRPresenter<Unit, Unit, AssignDelegatesView>() {


    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)

    override fun Unit.changeInitialModel(i: Unit): Unit = Unit

    override fun bindIntents() {

        val observable = loadRefreshPartialChanges();

        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                Unit)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                AssignDelegatesView::render)
    }
}