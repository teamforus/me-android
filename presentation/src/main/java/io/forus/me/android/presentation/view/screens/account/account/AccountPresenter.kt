package io.forus.me.android.presentation.view.screens.account.account

import io.forus.me.android.presentation.view.base.lr.LRPartialChange
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class AccountPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<AccountModel, AccountModel, AccountView>() {


    override fun initialModelSingle(): Single<AccountModel> = Single.zip(
            Single.fromObservable(accountRepository.getAccount()),
            Single.fromObservable(accountRepository.checkPin("")),
            BiFunction { account, pinIsEmpty ->
                AccountModel(account, !pinIsEmpty)
            })


    override fun AccountModel.changeInitialModel(i: AccountModel): AccountModel = i.copy()


    override fun bindIntents() {

        var observable = Observable.merge(
                loadRefreshPartialChanges(),
                intent { it.logout() }
                        .switchMap {
                            accountRepository.exitIdentity()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        AccountPartialChanges.NavigateToWelcomeScreen(true)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                                    .startWith(LRPartialChange.LoadingStarted)
                        }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                AccountModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                AccountView::render)
    }

    override fun stateReducer(viewState: LRViewState<AccountModel>, change: PartialChange): LRViewState<AccountModel> {

        if (change !is AccountPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {
            is AccountPartialChanges.NavigateToWelcomeScreen -> viewState.copy(model = viewState.model.copy(navigateToWelcome = true))

        }

    }
}