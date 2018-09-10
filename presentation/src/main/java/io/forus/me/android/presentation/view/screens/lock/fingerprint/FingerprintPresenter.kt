package io.forus.me.android.presentation.view.screens.lock.fingerprint

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class FingerprintPresenter constructor() : LRPresenter<Unit, FingerprintModel, FingerprintView>() {

    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)

    override fun FingerprintModel.changeInitialModel(i: Unit): FingerprintModel = copy()

    private val checkPin = PublishSubject.create<String>()
    private fun checkPin(): Observable<String> = checkPin

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.exit() }
                        .map { FingerprintPartialChanges.Exit(it) }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                FingerprintModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                FingerprintView::render)

    }

    override fun stateReducer(vs: LRViewState<FingerprintModel>, change: PartialChange): LRViewState<FingerprintModel> {

        if (change !is FingerprintPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is FingerprintPartialChanges.UnlockSuccess -> vs.copy(closeScreen = true, model = vs.model.copy(unlockSuccess = true))
            is FingerprintPartialChanges.Exit -> vs.copy(closeScreen = true, model = vs.model.copy(usePin = true))
        }
    }
}