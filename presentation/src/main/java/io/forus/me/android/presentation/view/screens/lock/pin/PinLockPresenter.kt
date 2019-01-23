package io.forus.me.android.presentation.view.screens.lock.pin

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRPartialChange
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PinLockPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<Unit, PinLockModel, PinLockView>() {

    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)

    override fun PinLockModel.changeInitialModel(i: Unit): PinLockModel = copy()

    private val checkPin = PublishSubject.create<String>()
    private fun checkPin(): Observable<String> = checkPin

    override fun bindIntents() {

        val observable = Observable.mergeArray(

                loadRefreshPartialChanges(),

                Observable.mergeArray(
                        intent { it.pinOnComplete() }
                                .map { PinLockPartialChanges.PinOnComplete(it) },
                        intent { it.pinOnChange() }
                                .map { PinLockPartialChanges.PinOnChange(it) }
                ),

                intent { it.exit() }
                        .switchMap {
                            accountRepository.exitIdentity()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMap<PartialChange> {
                                        Injection.instance.fcmHandler.clearFCMToken()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .map<PartialChange> { PinLockPartialChanges.Exit(Unit) }
                                                .onErrorReturn { LRPartialChange.LoadingError(it) }
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                        },

                intent { checkPin() }
                        .switchMap {
                            accountRepository.unlockIdentity(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if (it) PinLockPartialChanges.CheckPinSuccess(Unit)
                                        else PinLockPartialChanges.CheckPinError(Unit)
                                    }
                                    .onErrorReturn {
                                        PinLockPartialChanges.CheckPinError(Unit)
                                    }
                        },
                intent { it.logout() }
                        .switchMap {
                            accountRepository
                                    .exitIdentity()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .single(false)
                                    .map {
                                        PinLockPartialChanges.ExitIdentity(Unit)
                                    }
                                    .toObservable()
                        }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                PinLockModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                PinLockView::render)

    }

    override fun stateReducer(vs: LRViewState<PinLockModel>, change: PartialChange): LRViewState<PinLockModel> {

        if (change !is PinLockPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is PinLockPartialChanges.PinOnComplete -> {
                when (vs.model.state) {
                    PinLockModel.State.CONFIRM -> {
                        checkPin.onNext(change.passcode)
                        vs.copy(model = vs.model.changeState(PinLockModel.State.CHECKING))
                    }
                    else -> {
                        vs.copy(model = vs.model.changeState(vs.model.state))
                    }
                }
            }
            is PinLockPartialChanges.PinOnChange -> {
                when (vs.model.state) {
                    PinLockModel.State.WRONG_PIN -> vs.copy(model = vs.model.changeState(PinLockModel.State.CONFIRM))
                    else -> {
                        vs.copy(model = vs.model.changeState())
                    }
                }
            }

            is PinLockPartialChanges.CheckPinError -> vs.copy(model = vs.model.changeState(PinLockModel.State.WRONG_PIN))
            is PinLockPartialChanges.CheckPinSuccess -> vs.copy(closeScreen = true, model = vs.model.changeState(PinLockModel.State.SUCCESS))
            is PinLockPartialChanges.Exit -> vs.copy(closeScreen = true)
            is PinLockPartialChanges.ExitIdentity -> vs.copy(exitIdentity = true)
        }
    }
}