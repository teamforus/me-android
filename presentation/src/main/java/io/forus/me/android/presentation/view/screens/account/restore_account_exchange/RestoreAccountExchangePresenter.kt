package io.forus.me.android.presentation.view.screens.account.restore_account_exchange

import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RestoreAccountExchangePresenter constructor(
    private val token: String,
    private val accountRepository: AccountRepository
) : LRPresenter<String?, RestoreAccountExchangeModel, RestoreAccountExchangeView>() {

    override fun initialModelSingle(): Single<String?> {
        return if (token.isBlank()) {
            Single.error(Exception("Invalid restore token"))
        } else {
            Single.fromObservable(accountRepository.restoreExchangeToken(token).map { it.accessToken })
        }
    }

    override fun RestoreAccountExchangeModel.changeInitialModel(i: String?): RestoreAccountExchangeModel {
        return copy(accessToken = i, exchangingToken = false, exchangeTokenError = null)
    }

    override fun bindIntents() {
        val observable = Observable.merge(
            loadRefreshPartialChanges(),
            intent { it.exchangeToken() }
                .flatMap { exchangeToken(it) }
        )

        val initialViewState = LRViewState(
            false,
            null,
            false,
            false,
            null,
            false,
            RestoreAccountExchangeModel(),
            false
        )

        subscribeViewState(
            observable.scan(initialViewState, this::stateReducer)
                .observeOn(AndroidSchedulers.mainThread()),
            RestoreAccountExchangeView::render
        )
    }

    private fun exchangeToken(token: String): Observable<PartialChange> {
        return accountRepository.restoreExchangeToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<PartialChange> {
                RestoreAccountExchangePartialChanges.ExchangeTokenResult(it.accessToken)
            }
            .onErrorReturn {
                RestoreAccountExchangePartialChanges.ExchangeTokenError(it)
            }
            .startWith(RestoreAccountExchangePartialChanges.ExchangeTokenStart())
    }

    override fun stateReducer(
        vs: LRViewState<RestoreAccountExchangeModel>,
        change: PartialChange
    ): LRViewState<RestoreAccountExchangeModel> {
        if (change !is RestoreAccountExchangePartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RestoreAccountExchangePartialChanges.ExchangeTokenStart -> vs.copy(
                loadingError = null,
                model = vs.model.copy(accessToken = null, exchangingToken = true, exchangeTokenError = null)
            )
            is RestoreAccountExchangePartialChanges.ExchangeTokenResult -> vs.copy(
                loadingError = null,
                model = vs.model.copy(
                    accessToken = change.accessToken,
                    exchangingToken = false,
                    exchangeTokenError = null
                )
            )
            is RestoreAccountExchangePartialChanges.ExchangeTokenError -> vs.copy(
                loadingError = null,
                model = vs.model.copy(accessToken = null, exchangingToken = false, exchangeTokenError = change.error)
            )
        }
    }
}
