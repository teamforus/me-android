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

                intent { it.register() }
                        .switchMap {
                            accountRepository.restoreByEmail(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        if (it) LogInSignUpPartialChanges.RestoreByEmailRequestEnd()
                                        else LogInSignUpPartialChanges.RestoreByEmailRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        LogInSignUpPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(LogInSignUpPartialChanges.RestoreByEmailRequestStart())

                        },

                intent { it.validateEmail() }
                        .switchMap {
                            accountRepository.validateEmail(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        LogInSignUpPartialChanges.ValidateEmailRequest(it)
                                        // else LogInSignUpPartialChanges.RestoreByEmailRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        LogInSignUpPartialChanges.ValidateEmailRequestError(it)
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
                        },

                intent { it.registerNewAccount() }
                        .switchMap {
                            accountRepository.newUser(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        //NewAccountPartialChanges.RegisterEnd(it)
                                        if (it) LogInSignUpPartialChanges.RestoreByEmailRequestEnd()
                                        else LogInSignUpPartialChanges.RestoreByEmailRequestError(Exception(it.toString()))
                                    }
                                    .onErrorReturn {
                                        //NewAccountPartialChanges.RegisterError(it)
                                        LogInSignUpPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(//NewAccountPartialChanges.RegisterStart(it)
                                            LogInSignUpPartialChanges.RestoreByEmailRequestStart()
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
            is LogInSignUpPartialChanges.RestoreByEmailRequestStart -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = true,
                    sendingRestoreByEmailError = null, validateEmail = null, validateEmailError = null))
            is LogInSignUpPartialChanges.RestoreByEmailRequestEnd -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false,
                    sendingRestoreByEmailSuccess = true, validateEmail = null, validateEmailError = null))
            is LogInSignUpPartialChanges.RestoreByEmailRequestError -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false,
                    sendingRestoreByEmailError = change.error, validateEmail = null, validateEmailError = null))
            is LogInSignUpPartialChanges.ExchangeTokenResult -> vs.copy(model = vs.model.copy(accessToken = change.accessToken, sendingRestoreByEmail = false,
                    sendingRestoreByEmailError = null, validateEmail = null, validateEmailError = null))
            is LogInSignUpPartialChanges.ExchangeTokenError -> vs.copy(model = vs.model.copy(exchangeTokenError = change.error,
                    validateEmail = null, validateEmailError = null))
            is LogInSignUpPartialChanges.ValidateEmailRequest -> vs.copy(model = vs.model.copy(validateEmail = change.validateEmail, validateEmailError = null,
                    sendingRestoreByEmail = false, sendingRestoreByEmailError = null, sendingRestoreByEmailSuccess = false))
            is LogInSignUpPartialChanges.ValidateEmailRequestError -> vs.copy(model = vs.model.copy(validateEmailError = change.error, validateEmail = null,
                    sendingRestoreByEmail = false, sendingRestoreByEmailError = null, sendingRestoreByEmailSuccess = false))
        }
    }
}