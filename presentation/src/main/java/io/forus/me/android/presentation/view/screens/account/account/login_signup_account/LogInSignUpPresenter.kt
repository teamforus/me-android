package io.forus.me.android.presentation.view.screens.account.account.login_signup_account

import android.util.Log
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRPartialChange
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailModel
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailPartialChanges
import io.forus.me.android.presentation.view.screens.account.assigndelegates.email.RestoreByEmailView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class LogInSignUpPresenter constructor(private val token: String, private val accountRepository: AccountRepository) :
        LRPresenter<String?, LogInSignUpModel, LogInSignUpView>() {

    override fun initialModelSingle(): Single<String?> {
        return if(token.isBlank())
            Single.just("")
        else
            Single.fromObservable(accountRepository.restoreExchangeToken(token).map { it.accessToken })    }


    override fun LogInSignUpModel.changeInitialModel(i: String?): LogInSignUpModel {
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
                                        if(it) LogInSignUpPartialChanges.RestoreByEmailRequestEnd()
                                        else LogInSignUpPartialChanges.RestoreByEmailRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        LogInSignUpPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(LogInSignUpPartialChanges.RestoreByEmailRequestStart())

                        },

                intent { it.exchangeToken() }
                        .flatMap {
                            accountRepository.restoreExchangeToken(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        LogInSignUpPartialChanges.ExchangeTokenResult(it.accessToken)
                                    }
                                    .onErrorReturn {
                                        LogInSignUpPartialChanges.ExchangeTokenError(it)
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
                io.forus.me.android.presentation.view.screens.account.account.login_signup_account.LogInSignUpModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                LogInSignUpView::render)
    }

    override fun stateReducer(vs: LRViewState<LogInSignUpModel>, change: PartialChange): LRViewState<LogInSignUpModel> {

        if (change !is LogInSignUpPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is LogInSignUpPartialChanges.RestoreByEmailRequestStart -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = true, sendingRestoreByEmailError = null))
            is LogInSignUpPartialChanges.RestoreByEmailRequestEnd -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailSuccess = true))
            is LogInSignUpPartialChanges.RestoreByEmailRequestError -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailError = change.error))
            is LogInSignUpPartialChanges.ExchangeTokenResult -> vs.copy(model = vs.model.copy(accessToken = change.accessToken, sendingRestoreByEmail = false, sendingRestoreByEmailError = null))
            is LogInSignUpPartialChanges.ExchangeTokenError -> vs.copy(model = vs.model.copy(exchangeTokenError = change.error))
        }
    }
}