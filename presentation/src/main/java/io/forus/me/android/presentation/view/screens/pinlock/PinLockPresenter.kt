package io.forus.me.android.presentation.view.screens.pinlock

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.repository.account.AccountRepository
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

        val observable = Observable.merge(

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
                                    .map<PartialChange> {
                                        PinLockPartialChanges.Exit(Unit)
                                    }
                                    .onErrorReturn {
                                        PinLockPartialChanges.Exit(Unit)
                                    }
                        },

                intent { checkPin() }
                        .switchMap {
                            accountRepository.unlockIdentity(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) PinLockPartialChanges.CheckPinSuccess(Unit)
                                        else PinLockPartialChanges.CheckPinError(Unit)
                                    }
                                    .onErrorReturn {
                                        PinLockPartialChanges.CheckPinError(Unit)
                                    }
                        }
        );


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                PinLockModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                PinLockView::render)

    }

    override fun stateReducer(vs: LRViewState<PinLockModel>, change: PartialChange): LRViewState<PinLockModel> {

        if (change !is PinLockPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is PinLockPartialChanges.PinOnComplete -> {
                when(vs.model.state){
                    PinLockModel.State.CONFIRM -> {
                        checkPin.onNext(change.passcode)
                        vs.copy(model = vs.model.changeState(PinLockModel.State.CHECKING))
                    }
                    else -> { vs.copy(model = vs.model.changeState(vs.model.state))}
                }
            }
            is PinLockPartialChanges.PinOnChange -> {
                when(vs.model.state){
                    PinLockModel.State.WRONG_PIN -> vs.copy(model = vs.model.changeState(PinLockModel.State.CONFIRM))
                    else -> { vs.copy(model = vs.model.changeState())}
                }
            }

            is PinLockPartialChanges.CheckPinError -> vs.copy(model = vs.model.changeState(PinLockModel.State.WRONG_PIN))
            is PinLockPartialChanges.CheckPinSuccess -> vs.copy(closeScreen = true, model = vs.model.changeState(PinLockModel.State.SUCCESS))
            is PinLockPartialChanges.Exit -> vs.copy(closeScreen = true)
        }
    }
}