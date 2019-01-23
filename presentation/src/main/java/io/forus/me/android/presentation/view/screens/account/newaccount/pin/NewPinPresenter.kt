package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import io.forus.me.android.domain.models.account.Identity
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class NewPinPresenter constructor(private val accountRepository: AccountRepository, private val accessToken: String) : LRPresenter<String, NewPinModel, NewPinView>() {

    override fun initialModelSingle(): Single<String> = Single.just(accessToken)

    override fun NewPinModel.changeInitialModel(i: String): NewPinModel = copy(accessToken=i)

    private val createIdentity = PublishSubject.create<Identity>()
    private fun createIdentity(): Observable<Identity> = createIdentity

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                Observable.merge(

                    intent { it.pinOnComplete() }
                            .map { NewPinPartialChanges.PinOnComplete(it) },

                    intent { it.pinOnChange() }
                            .map { NewPinPartialChanges.PinOnChange(it) },

                    intent { it.skip() }
                            .map {  NewPinPartialChanges.SkipPin() }
                ),

                intent { createIdentity() }
                        .switchMap {
                            accountRepository.createIdentity(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        NewPinPartialChanges.CreateIdentityEnd(Unit)
                                    }
                                    .onErrorReturn {
                                        NewPinPartialChanges.CreateIdentityError(it)
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
                NewPinModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                NewPinView::render)

    }

    override fun stateReducer(vs: LRViewState<NewPinModel>, change: PartialChange): LRViewState<NewPinModel> {

        if (change !is NewPinPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is NewPinPartialChanges.PinOnComplete -> {
                when(vs.model.state){
                    NewPinModel.State.CREATE -> vs.copy(model = vs.model.changeState(NewPinModel.State.CONFIRM, change.passcode))
                    NewPinModel.State.CONFIRM -> {
                        if(vs.model.passcode.equals(change.passcode) && vs.model.valid){
                            createIdentity.onNext(Identity(vs.model.accessToken!!, vs.model.passcode!!))
                            vs.copy(model = vs.model.changeState(NewPinModel.State.CREATING_IDENTITY))
                        }
                        else{
                            vs.copy(model = vs.model.changeState(NewPinModel.State.PASS_NOT_MATCH, skipEnabled = true))
                        }
                    }
                    else -> { vs.copy(model = vs.model.changeState())}
                }
            }
            is NewPinPartialChanges.PinOnChange -> {
                when(vs.model.state){
                    NewPinModel.State.CREATE -> vs.copy(model = vs.model.changeState(skipEnabled = change.passcode.isEmpty()))
                    NewPinModel.State.PASS_NOT_MATCH -> vs.copy(model = vs.model.changeState(NewPinModel.State.CREATE, null, skipEnabled = change.passcode.isEmpty()))
                    else -> { vs.copy(model = vs.model.changeState())}
                }
            }

            is NewPinPartialChanges.SkipPin -> {
                if(vs.model.skipEnabled) createIdentity.onNext(Identity(accessToken,""))
                vs.copy(model = vs.model.changeState(NewPinModel.State.CREATING_IDENTITY))
            }

            is NewPinPartialChanges.CreateIdentityError -> vs.copy(model = vs.model.changeState(NewPinModel.State.CREATING_IDENTITY_ERROR).copy(createIdentityError = change.error))
            is NewPinPartialChanges.CreateIdentityEnd -> vs.copy(closeScreen = true)
        }
    }
}