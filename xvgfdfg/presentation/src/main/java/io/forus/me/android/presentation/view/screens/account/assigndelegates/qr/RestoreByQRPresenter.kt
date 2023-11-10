package io.forus.me.android.presentation.view.screens.account.assigndelegates.qr

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.helpers.reactivex.AccessTokenChecker
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class RestoreByQRPresenter constructor(private val disposableHolder: DisposableHolder, private val accessTokenChecker: AccessTokenChecker, private val accountRepository: AccountRepository) : LRPresenter<RequestDelegatesQrModel, RestoreByQRModel, RestoreByQRView>() {

    override fun initialModelSingle(): Single<RequestDelegatesQrModel> = Single.fromObservable(accountRepository.restoreByQrToken())

    override fun RestoreByQRModel.changeInitialModel(i: RequestDelegatesQrModel): RestoreByQRModel = copy(item = i).also {
        disposableHolder.add(accessTokenChecker.startCheckingActivation(i.accessToken, activationComplete))
    }

    private val activationComplete = PublishSubject.create<Unit>()
    fun activationComplete(): Observable<Unit> = activationComplete

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { activationComplete() }.map { RestoreByQRPartialChanges.RestoreIdentity() }
        )

        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RestoreByQRModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RestoreByQRView::render)
    }

    override fun stateReducer(vs: LRViewState<RestoreByQRModel>, change: PartialChange): LRViewState<RestoreByQRModel> {

        if (change !is RestoreByQRPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RestoreByQRPartialChanges.RestoreIdentity -> vs.copy(closeScreen = true, model = vs.model.copy(isQrConfirmed = true))
        }

    }
}