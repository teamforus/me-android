package io.forus.me.android.presentation.view.screens.account.assigndelegates

import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.helpers.reactivex.AccessTokenChecker
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject


class AssignDelegatesPresenter constructor(private val disposableHolder: DisposableHolder, private val accessTokenChecker: AccessTokenChecker, private val accountRepository: AccountRepository)
    : LRPresenter<RequestDelegatesPinModel, AssignDelegatesAccountModel, AssignDelegatesView>() {

    override fun initialModelSingle(): Single<RequestDelegatesPinModel> = Single.fromObservable(accountRepository.restoreByPinCode())

    override fun AssignDelegatesAccountModel.changeInitialModel(i: RequestDelegatesPinModel): AssignDelegatesAccountModel = copy(item = i).also {
        disposableHolder.add(accessTokenChecker.startCheckingActivation(i.accessToken, activationComplete))
    }

    private val activationComplete = PublishSubject.create<Unit>()
    fun activationComplete(): Observable<Unit> = activationComplete

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { activationComplete() }.map { AssignDelegatesAccountPartialChanges.RestoreIdentity() }
        )

        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                AssignDelegatesAccountModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                AssignDelegatesView::render)
    }

    override fun stateReducer(vs: LRViewState<AssignDelegatesAccountModel>, change: PartialChange): LRViewState<AssignDelegatesAccountModel> {

        if (change !is AssignDelegatesAccountPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is AssignDelegatesAccountPartialChanges.RestoreIdentity -> vs.copy(closeScreen = true, model = vs.model.copy(isPinConfirmed = true))
        }

    }
}