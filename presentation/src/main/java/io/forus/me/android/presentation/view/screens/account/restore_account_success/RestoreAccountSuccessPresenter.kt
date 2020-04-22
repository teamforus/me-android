package io.forus.me.android.presentation.view.screens.account.restore_account_success

import android.util.Log
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by maestrovs on 22.04.2020.
 */
class RestoreAccountSuccessPresenter constructor(private val token: String, private val accountRepository: AccountRepository) :
        LRPresenter<String?, RestoreAccountSuccessModel, RestoreAccountSuccessView>() {

    override fun initialModelSingle(): Single<String?> {
        return if(token.isBlank())
            Single.just("")
        else
            Single.fromObservable(accountRepository.restoreExchangeToken(token).map { it.accessToken })
    }

    override fun RestoreAccountSuccessModel.changeInitialModel(i: String?): RestoreAccountSuccessModel{
        return copy(accessToken = i)
    }

    override fun bindIntents() {

        Log.d("meforus","RestoreByEmailPresenter**")

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.register() }
                        .switchMap {
                            accountRepository.restoreByEmail(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) RestoreAccountSuccessPartialChanges.RestoreByEmailRequestEnd()
                                        else RestoreAccountSuccessPartialChanges.RestoreByEmailRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        RestoreAccountSuccessPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(RestoreAccountSuccessPartialChanges.RestoreByEmailRequestStart())

                        },

                intent { it.exchangeToken() }
                        .flatMap {
                            accountRepository.restoreExchangeToken(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        RestoreAccountSuccessPartialChanges.ExchangeTokenResult(it.accessToken)
                                    }
                                    .onErrorReturn {
                                        RestoreAccountSuccessPartialChanges.ExchangeTokenError(it)
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
                RestoreAccountSuccessModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RestoreAccountSuccessView::render)
    }

    override fun stateReducer(vs: LRViewState<RestoreAccountSuccessModel>, change: PartialChange): LRViewState<RestoreAccountSuccessModel> {

        if (change !is RestoreAccountSuccessPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RestoreAccountSuccessPartialChanges.RestoreByEmailRequestStart -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = true, sendingRestoreByEmailError = null))
            is RestoreAccountSuccessPartialChanges.RestoreByEmailRequestEnd -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailSuccess = true))
            is RestoreAccountSuccessPartialChanges.RestoreByEmailRequestError -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailError = change.error))
            is RestoreAccountSuccessPartialChanges.ExchangeTokenResult -> vs.copy(model = vs.model.copy(accessToken = change.accessToken, sendingRestoreByEmail = false, sendingRestoreByEmailError = null))
            is RestoreAccountSuccessPartialChanges.ExchangeTokenError -> vs.copy(model = vs.model.copy(exchangeTokenError = change.error))
        }
    }
}