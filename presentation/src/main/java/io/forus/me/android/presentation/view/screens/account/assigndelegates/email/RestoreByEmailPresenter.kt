package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.helpers.reactivex.AccessTokenChecker
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class RestoreByEmailPresenter constructor(private val disposableHolder: DisposableHolder, private val accessTokenChecker: AccessTokenChecker, private val accountRepository: AccountRepository) : LRPresenter<Unit, RestoreByEmailModel, RestoreByEmailView>() {

    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)


    override fun RestoreByEmailModel.changeInitialModel(i: Unit): RestoreByEmailModel = copy()

    private val activationComplete = PublishSubject.create<Unit>()
    fun activationComplete(): Observable<Unit> = activationComplete

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),
                intent { activationComplete() }.map { RestoreByEmailPartialChanges.RestoreIdentity() },
                intent { it.register() }
                        .switchMap {
                            accountRepository.restoreByEmail(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        disposableHolder.add(accessTokenChecker.startCheckingActivation(it.accessToken, activationComplete))
                                        RestoreByEmailPartialChanges.RestoreByEmailRequestEnd(it)
                                    }
                                    .onErrorReturn {
                                        RestoreByEmailPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(RestoreByEmailPartialChanges.RestoreByEmailRequestStart())

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
            is RestoreByEmailPartialChanges.RestoreByEmailRequestEnd -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = true, item = change.requestDelegatesEmailModel))
            is RestoreByEmailPartialChanges.RestoreByEmailRequestError -> vs.copy(model = vs.model.copy(sendingRestoreByEmail = false, sendingRestoreByEmailError = change.error))
            is RestoreByEmailPartialChanges.RestoreIdentity -> vs.copy(closeScreen = true, model = vs.model.copy(isEmailConfirmed = true))
        }
    }
}