package io.forus.me.android.presentation.view.screens.account.login_signup_account

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LogInSignUpPresenter constructor(private val token: String, private val accountRepository: AccountRepository) :
        LRPresenter<String?, LogInSignUpModel, LogInSignUpView>() {

    override fun initialModelSingle(): Single<String?> {
        return if (token.isBlank())
            Single.just("")
        else {
            Single.fromObservable(accountRepository.restoreExchangeToken(token).map { it.accessToken })
        }
    }


    override fun LogInSignUpModel.changeInitialModel(i: String?): LogInSignUpModel {
        return copy(accessToken = i)
    }

    override fun bindIntents() {


        val observable = Observable.mergeArray(

                loadRefreshPartialChanges(),

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
                        },

                        intent { it.registerNewAccount() }
                        .switchMap {
                            accountRepository.newUser(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if(it) LogInSignUpPartialChanges.EmailAuthRequestEnd()
                                        else LogInSignUpPartialChanges.EmailAuthRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        LogInSignUpPartialChanges.EmailAuthRequestError(it)
                                    }
                                    .startWith(
                                            LogInSignUpPartialChanges.EmailAuthRequestStart()
                                    )
                        }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                io.forus.me.android.presentation.view.screens.account.login_signup_account.LogInSignUpModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                LogInSignUpView::render)
    }

    override fun stateReducer(vs: LRViewState<LogInSignUpModel>, change: PartialChange): LRViewState<LogInSignUpModel> {

        if (change !is LogInSignUpPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is LogInSignUpPartialChanges.EmailAuthRequestStart -> vs.copy(model = vs.model.copy(
                    sendingEmailAuth = true,
                    sendingEmailAuthSuccess = false,
                    emailAuthError = null))
            is LogInSignUpPartialChanges.EmailAuthRequestEnd -> vs.copy(model = vs.model.copy(
                    sendingEmailAuth = false,
                    emailAuthError = null,
                    sendingEmailAuthSuccess = true))
            is LogInSignUpPartialChanges.EmailAuthRequestError -> vs.copy(model = vs.model.copy(
                    sendingEmailAuth = false,
                    sendingEmailAuthSuccess = false,
                    emailAuthError = change.error))
            is LogInSignUpPartialChanges.ExchangeTokenResult -> vs.copy(model = vs.model.copy(
                    accessToken = change.accessToken,
                    sendingEmailAuth = false,
                    emailAuthError = null))
            is LogInSignUpPartialChanges.ExchangeTokenError -> vs.copy(model = vs.model.copy(
                    exchangeTokenError = change.error))
        }
    }
}
