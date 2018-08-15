package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.Identity
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class NewPinPresenter constructor(private val accountRepository: AccountRepository, private val accessToken: String) : LRPresenter<String, NewPinModel, NewPinView>() {

    override fun initialModelSingle(): Single<String> = Single.just(accessToken)

    override fun NewPinModel.changeInitialModel(i: String): NewPinModel = copy(access_token=i)

    private val createIdentity = PublishSubject.create<Identity>()
    private fun createIdentity(): Observable<Identity> = createIdentity

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.pinOnComplete() }
                        .map { NewPinPartialChanges.PinOnComplete(it) },

                intent { it.pinOnChange() }
                        .map { NewPinPartialChanges.PinOnChange(it) },

                intent { createIdentity() }
                        .switchMap {
                            accountRepository.createIdentity(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) NewPinPartialChanges.CreateIdentityEnd(Unit)
                                        else NewPinPartialChanges.CreateIdentityError(Unit)
                                    }
                                    .onErrorReturn {
                                        NewPinPartialChanges.CreateIdentityError(Unit)
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
                NewPinModel())

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
                    NewPinModel.State.CREATE -> vs.copy(model = vs.model.copy(passcode = change.passcode, prevState = vs.model.state, state = NewPinModel.State.CONFIRM))
                    NewPinModel.State.CONFIRM -> {
                        if(vs.model.passcode.equals(change.passcode) && vs.model.valid){
                            createIdentity.onNext(Identity(vs.model.access_token!!, vs.model.passcode!!))
                            vs.copy(model = vs.model.copy(prevState = vs.model.state, state = NewPinModel.State.CREATING_IDENTITY))
                        }
                        else{
                            vs.copy(model = vs.model.copy(prevState = vs.model.state, state = NewPinModel.State.PASS_NOT_MATCH))
                        }
                    }
                    else -> { vs.copy(model = vs.model.copy(prevState = vs.model.state))}
                }
            }
            is NewPinPartialChanges.PinOnChange -> {
                when(vs.model.state){
                    NewPinModel.State.PASS_NOT_MATCH -> vs.copy(model = vs.model.copy(passcode = null, prevState = vs.model.state, state = NewPinModel.State.CREATE))
                    else -> { vs.copy(model = vs.model.copy(prevState = vs.model.state))}
                }

            }

            is NewPinPartialChanges.CreateIdentityError -> vs.copy(model = vs.model.copy(prevState = vs.model.state, state = NewPinModel.State.CREATING_IDENTITY_ERROR))
            is NewPinPartialChanges.CreateIdentityEnd -> vs.copy(closeScreen = true)
        }
    }
}