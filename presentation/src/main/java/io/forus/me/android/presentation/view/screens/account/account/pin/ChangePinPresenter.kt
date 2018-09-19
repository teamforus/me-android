package io.forus.me.android.presentation.view.screens.account.account.pin

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.ChangePin
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.models.ChangePinMode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ChangePinPresenter constructor(private val mode: ChangePinMode, private val accountRepository: AccountRepository) : LRPresenter<Unit, ChangePinModel, ChangePinView>() {

    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)

    override fun ChangePinModel.changeInitialModel(i: Unit): ChangePinModel{
        val initialState = when(mode){
            ChangePinMode.SET_NEW -> ChangePinModel.State.CREATE_NEW_PIN
            ChangePinMode.REMOVE_OLD, ChangePinMode.CHANGE_OLD  -> ChangePinModel.State.CONFIRM_OLD_PIN
        }
        return copy(prevState = initialState, state = initialState)
    }

    private val checkPin = PublishSubject.create<String>()
    private fun checkPin(): Observable<String> = checkPin

    private val changePin = PublishSubject.create<ChangePin>()
    private fun changePin(): Observable<ChangePin> = changePin

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                Observable.mergeArray(
                        intent { it.pinOnComplete() }
                                .map { ChangePinPartialChanges.PinOnComplete(it) },
                        intent { it.pinOnChange() }
                                .map { ChangePinPartialChanges.PinOnChange(it) }
                ),

                intent { checkPin() }
                        .switchMap {
                            accountRepository.checkPin(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) ChangePinPartialChanges.CheckPinSuccess(Unit)
                                        else ChangePinPartialChanges.CheckPinError(Unit)
                                    }
                                    .onErrorReturn {
                                        ChangePinPartialChanges.CheckPinError(Unit)
                                    }
                        },

                intent { changePin() }
                        .switchMap {
                            accountRepository.changePin(it.oldPin, it.newPin)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) ChangePinPartialChanges.ChangePinEnd(Unit)
                                        else ChangePinPartialChanges.ChangePinError(Unit)
                                    }
                                    .onErrorReturn {
                                        ChangePinPartialChanges.ChangePinError(Unit)
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
                ChangePinModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                ChangePinView::render)

    }

    override fun stateReducer(vs: LRViewState<ChangePinModel>, change: PartialChange): LRViewState<ChangePinModel> {

        if (change !is ChangePinPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is ChangePinPartialChanges.PinOnComplete -> {
                when(vs.model.state){
                    ChangePinModel.State.CONFIRM_OLD_PIN -> {
                        checkPin.onNext(change.passcode)
                        vs.copy(model = vs.model.changeState(ChangePinModel.State.CHECKING_OLD_PIN, passcodeOld = change.passcode))
                    }
                    ChangePinModel.State.CREATE_NEW_PIN -> vs.copy(model = vs.model.changeState(ChangePinModel.State.CONFIRM_NEW_PIN, passcodeNew = change.passcode))
                    ChangePinModel.State.CONFIRM_NEW_PIN -> {
                        if(vs.model.passcodeNew.equals(change.passcode) && vs.model.valid){
                            changePin.onNext(ChangePin(vs.model.passcodeOld, vs.model.passcodeNew!!))
                            vs.copy(model = vs.model.changeState(ChangePinModel.State.CHANGING_PIN))
                        }
                        else{
                            vs.copy(model = vs.model.changeState(ChangePinModel.State.PASS_NOT_MATCH))
                        }
                    }
                    else -> { vs.copy(model = vs.model.changeState(vs.model.state))}
                }
            }
            is ChangePinPartialChanges.PinOnChange -> {
                when(vs.model.state){
                    ChangePinModel.State.WRONG_OLD_PIN -> vs.copy(model = vs.model.changeState(ChangePinModel.State.CONFIRM_OLD_PIN))
                    ChangePinModel.State.PASS_NOT_MATCH -> vs.copy(model = vs.model.changeState(ChangePinModel.State.CREATE_NEW_PIN, passcodeNew = null))
                    else -> { vs.copy(model = vs.model.changeState())}
                }
            }
            is ChangePinPartialChanges.CheckPinError -> vs.copy(model = vs.model.changeState(ChangePinModel.State.WRONG_OLD_PIN))
            is ChangePinPartialChanges.CheckPinSuccess -> {
                if(mode == ChangePinMode.REMOVE_OLD) {
                    changePin.onNext(ChangePin(vs.model.passcodeOld, ""))
                    vs.copy(model = vs.model.changeState(ChangePinModel.State.CHANGING_PIN))
                }
                else vs.copy(model = vs.model.changeState(ChangePinModel.State.CREATE_NEW_PIN))
            }

            is ChangePinPartialChanges.ChangePinError -> vs.copy(model = vs.model.changeState(ChangePinModel.State.CHANGE_PIN_ERROR))
            is ChangePinPartialChanges.ChangePinEnd -> vs.copy(closeScreen = true)
        }
    }
}