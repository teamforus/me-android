package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RestoreByEmailPresenter constructor(private val token: String, private val accountRepository: AccountRepository) :
        LRPresenter<String?, RestoreByEmailModel, RestoreByEmailView>() {

    override fun initialModelSingle(): Single<String?> {
        return if(token.isBlank())
            Single.just("")
        else
            Single.fromObservable(accountRepository.restoreExchangeToken(token).map { it.accessToken })
    }

    override fun RestoreByEmailModel.changeInitialModel(i: String?): RestoreByEmailModel{
        return copy(accessToken = i)
    }

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.register() }
                        .switchMap {
                            accountRepository.restoreByEmail(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) RestoreByEmailPartialChanges.RestoreByEmailRequestEnd()
                                        else RestoreByEmailPartialChanges.RestoreByEmailRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        RestoreByEmailPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(RestoreByEmailPartialChanges.RestoreByEmailRequestStart())

                        },

                intent { it.exchangeToken() }
                        .flatMap {
                            accountRepository.restoreExchangeToken(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        RestoreByEmailPartialChanges.ExchangeTokenResult(it.accessToken)
                                    }
                                    .onErrorReturn {
                                        RestoreByEmailPartialChanges.ExchangeTokenError(it)
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
                RestoreByEmailModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RestoreByEmailView::render)
    }

    override fun stateReducer(vs: LRViewState<RestoreByEmailModel>, change: PartialChange): LRViewState<RestoreByEmailModel> {

        if (change !is RestoreByEmailPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RestoreByEmailPartialChanges.RestoreByEmailRequestStart -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = true, sendingRestoreByEmailError = null))
            is RestoreByEmailPartialChanges.RestoreByEmailRequestEnd -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailSuccess = true))
            is RestoreByEmailPartialChanges.RestoreByEmailRequestError -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailError = change.error))
            is RestoreByEmailPartialChanges.ExchangeTokenResult -> vs.copy(model = vs.model.copy(accessToken = change.accessToken, sendingRestoreByEmail = false, sendingRestoreByEmailError = null))
            is RestoreByEmailPartialChanges.ExchangeTokenError -> vs.copy(model = vs.model.copy(exchangeTokenError = change.error))
        }
    }
}