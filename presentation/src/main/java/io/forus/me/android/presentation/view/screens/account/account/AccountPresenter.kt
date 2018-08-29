package io.forus.me.android.presentation.view.screens.account.newaccount

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPartialChange
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.Account
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AccountPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<AccountModel, AccountModel, AccountView>() {


    override fun initialModelSingle(): Single<AccountModel> = Single.fromObservable(accountRepository.getAccount())
            .flatMap {  Single.just(AccountModel(it))  }


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