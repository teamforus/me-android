package io.forus.me.android.presentation.view.screens.account.assigndelegates.pin

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.helpers.reactivex.AccessTokenChecker
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject


class RestoreByPinPresenter constructor(private val disposableHolder: DisposableHolder, private val accessTokenChecker: AccessTokenChecker, private val accountRepository: AccountRepository) : LRPresenter<RequestDelegatesPinModel, RestoreByPinModel, RestoreByPinView>() {

    override fun initialModelSingle(): Single<RequestDelegatesPinModel> = Single.fromObservable(accountRepository.restoreByPinCode())

    override fun RestoreByPinModel.changeInitialModel(i: RequestDelegatesPinModel): RestoreByPinModel = copy(item = i).also {
        disposableHolder.add(accessTokenChecker.startCheckingActivation(i.accessToken, activationComplete))
    }

    private val activationComplete = PublishSubject.create<Unit>()
    fun activationComplete(): Observable<Unit> = activationComplete

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { activationComplete() }.map { RestoreByPinPartialChanges.RestoreIdentity() }
        )

        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RestoreByPinModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RestoreByPinView::render)
    }

    override fun stateReducer(vs: LRViewState<RestoreByPinModel>, change: PartialChange): LRViewState<RestoreByPinModel> {

        if (change !is RestoreByPinPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RestoreByPinPartialChanges.RestoreIdentity -> vs.copy(closeScreen = true, model = vs.model.copy(isPinConfirmed = true))
        }

    }
}