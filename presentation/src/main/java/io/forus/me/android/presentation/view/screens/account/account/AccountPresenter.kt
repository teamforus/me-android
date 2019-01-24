package io.forus.me.android.presentation.view.screens.account.account

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRPartialChange
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class AccountPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<AccountModel, AccountModel, AccountView>() {


    override fun initialModelSingle(): Single<AccountModel> = Single.zip(
            Single.fromObservable(accountRepository.getAccount()),
            Single.fromObservable(accountRepository.getSecurityOptions()),
            BiFunction { account, securityOptions ->
                AccountModel(account, securityOptions.pinEnabled, securityOptions.fingerprintEnabled, securityOptions.startFromScanner, securityOptions.sendCrashReportsEnabled)
            })


    override fun AccountModel.changeInitialModel(i: AccountModel): AccountModel = i.copy()


    override fun bindIntents() {

        val observable = Observable.mergeArray(
                loadRefreshPartialChanges(),
                intent { it.logout() }
                        .switchMap {
                            accountRepository.exitIdentity()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMap<PartialChange> {
                                        Injection.instance.fcmHandler.clearFCMToken()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .map<PartialChange> { AccountPartialChanges.NavigateToWelcomeScreen(true) }
                                                .onErrorReturn { LRPartialChange.LoadingError(it) }
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                                    .startWith(LRPartialChange.LoadingStarted)
                        },

                intent { it.switchFingerprint() }
                        .switchMap { newState ->
                            accountRepository.setFingerprintEnabled(newState)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> { success ->
                                        AccountPartialChanges.FingerprintEnabled(if (success) newState else !newState)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                        },

                intent { it.switchStartFromScanner() }
                        .switchMap { newState ->
                            accountRepository.setStartFromScannerEnabled(newState)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> { success ->
                                        AccountPartialChanges.StartFromScannerEnabled(if (success) newState else !newState)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                        },

                intent { it.switchSendCrashReports() }
                        .switchMap { newState ->
                            accountRepository.setSendCrashReportsEnabled(newState)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> { success ->
                                        AccountPartialChanges.SendCrashReportsEnabled(if (success) newState else !newState)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
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
                AccountModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                AccountView::render)
    }

    override fun stateReducer(vs: LRViewState<AccountModel>, change: PartialChange): LRViewState<AccountModel> {

        if (change !is AccountPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is AccountPartialChanges.NavigateToWelcomeScreen -> vs.copy(model = vs.model.copy(navigateToWelcome = true))
            is AccountPartialChanges.FingerprintEnabled -> vs.copy(model = vs.model.copy(fingerprintEnabled = change.value))
            is AccountPartialChanges.StartFromScannerEnabled -> vs.copy(model = vs.model.copy(startFromScanner = change.value))
            is AccountPartialChanges.SendCrashReportsEnabled -> vs.copy(model = vs.model.copy(sendCrashReportsEnabled = change.value))
        }

    }
}