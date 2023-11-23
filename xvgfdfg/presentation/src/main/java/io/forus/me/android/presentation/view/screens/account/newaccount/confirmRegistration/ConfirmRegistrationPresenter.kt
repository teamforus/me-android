package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration

import android.util.Log
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ConfirmRegistrationPresenter constructor(private val token: String, private val accountRepository: AccountRepository) :
        LRPresenter<String?, ConfirmRegistrationModel, ConfirmRegistrationView>() {

    override fun initialModelSingle(): Single<String?> {
        return if(token.isBlank())
            Single.just("")
        else
            Single.fromObservable(accountRepository.registerExchangeToken(token).map { it.accessToken })
    }

    override fun ConfirmRegistrationModel.changeInitialModel(i: String?): ConfirmRegistrationModel{
        return copy(accessToken = i)
    }

    override fun bindIntents() {


        val observable = Observable.merge(

                loadRefreshPartialChanges(),



                intent { it.exchangeToken() }
                        .flatMap {
                            accountRepository.registerExchangeToken(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        ConfirmRegistrationPartialChanges.ExchangeTokenResult(it.accessToken)
                                    }
                                    .onErrorReturn {
                                        ConfirmRegistrationPartialChanges.ExchangeTokenError(it)
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
                ConfirmRegistrationModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                ConfirmRegistrationView::render)
    }

    override fun stateReducer(vs: LRViewState<ConfirmRegistrationModel>, change: PartialChange): LRViewState<ConfirmRegistrationModel> {

        if (change !is ConfirmRegistrationPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is ConfirmRegistrationPartialChanges.RestoreByEmailRequestStart -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = true, sendingRestoreByEmailError = null))
            is ConfirmRegistrationPartialChanges.RestoreByEmailRequestEnd -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailSuccess = true))
            is ConfirmRegistrationPartialChanges.RestoreByEmailRequestError -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailError = change.error))
            is ConfirmRegistrationPartialChanges.ExchangeTokenResult -> vs.copy(model = vs.model.copy(accessToken = change.accessToken, sendingRestoreByEmail = false, sendingRestoreByEmailError = null))
            is ConfirmRegistrationPartialChanges.ExchangeTokenError -> vs.copy(model = vs.model.copy(exchangeTokenError = change.error))
        }
    }
}