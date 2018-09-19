package io.forus.me.android.presentation.view.screens.account.newaccount

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NewAccountPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<NewAccountRequest, NewAccountModel, NewAccountView>() {


    override fun initialModelSingle(): Single<NewAccountRequest> = Single.just(NewAccountRequest())
            .flatMap {  Single.just(it)  }


    override fun NewAccountModel.changeInitialModel(i: NewAccountRequest): NewAccountModel = copy(item = i)


    override fun bindIntents() {

        var observable = Observable.merge(

                loadRefreshPartialChanges(),
                intent { it.register() }
                        .switchMap {
                            accountRepository.newUser(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        NewAccountPartialChanges.RegisterEnd(it)
                                    }
                                    .onErrorReturn {
                                        NewAccountPartialChanges.RegisterError(it)
                                    }
                                    .startWith(NewAccountPartialChanges.RegisterStart(it))
                        }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                NewAccountModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                NewAccountView::render)
    }

    override fun stateReducer(viewState: LRViewState<NewAccountModel>, change: PartialChange): LRViewState<NewAccountModel> {

        if (change !is NewAccountPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {
            is NewAccountPartialChanges.RegisterEnd -> viewState.copy(closeScreen = true, model = viewState.model.copy(sendingRegistration = false, accessToken = change.accessToken))
            is NewAccountPartialChanges.RegisterStart -> viewState.copy(model = viewState.model.copy(sendingRegistration = true, sendingRegistrationError = null))
            is NewAccountPartialChanges.RegisterError -> viewState.copy(model = viewState.model.copy(sendingRegistration = false, sendingRegistrationError = change.error))
        }

    }
}