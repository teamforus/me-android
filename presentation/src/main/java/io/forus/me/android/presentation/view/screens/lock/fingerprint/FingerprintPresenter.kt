package io.forus.me.android.presentation.view.screens.lock.fingerprint

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class FingerprintPresenter constructor(val accountRepository: AccountRepository) : LRPresenter<Unit, FingerprintModel, FingerprintView>() {

    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)

    override fun FingerprintModel.changeInitialModel(i: Unit): FingerprintModel = copy()

    private val checkPin = PublishSubject.create<String>()
    private fun checkPin(): Observable<String> = checkPin

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.exit() }
                        .map { FingerprintPartialChanges.Exit(it) },

                intent { it.authFail() }
                        .map { FingerprintPartialChanges.UnlockFail(it) },

                intent { it.authSuccess() }
                        .switchMap {
                            accountRepository.unlockByFingerprint()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map <FingerprintPartialChanges> {
                                        FingerprintPartialChanges.UnlockSuccess(Unit)
                                    }
                                    .onErrorReturn {
                                        FingerprintPartialChanges.UnlockFail(it.toString())
                                    }
                        }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                FingerprintModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                FingerprintView::render)

    }

    override fun stateReducer(vs: LRViewState<FingerprintModel>, change: PartialChange): LRViewState<FingerprintModel> {

        if (change !is FingerprintPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is FingerprintPartialChanges.Exit -> vs.copy(closeScreen = true, model = vs.model.copy(usePin = true))
            is FingerprintPartialChanges.UnlockFail -> vs.copy(model = vs.model.copy(unlockFail = true, unlockFailMessage = change.reason))
            is FingerprintPartialChanges.UnlockSuccess -> vs.copy(closeScreen = true, model = vs.model.copy(unlockSuccess = true))
        }
    }
}