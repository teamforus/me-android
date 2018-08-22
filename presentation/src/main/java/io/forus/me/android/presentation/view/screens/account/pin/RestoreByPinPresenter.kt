package io.forus.me.android.presentation.view.screens.account.pin

import com.gigawatt.android.data.net.sign.RecordsService
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.repository.account.datasource.remote.CheckActivationDataSource
import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.models.DisposableHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class RestoreByPinPresenter constructor(private val disposableHolder: DisposableHolder, private val accountRepository: AccountRepository) : LRPresenter<RequestDelegatesPinModel, RestoreByPinModel, RestoreByPinView>() {

    private val CHECK_ACTIVATION_DELAY = 1000L

    override fun initialModelSingle(): Single<RequestDelegatesPinModel> = Single.fromObservable(accountRepository.restoreByPinCode())

    override fun RestoreByPinModel.changeInitialModel(i: RequestDelegatesPinModel): RestoreByPinModel = copy(item = i).also {
        startCheckingActivation(i.accessToken)
    }

    private fun startCheckingActivation(accessToken: String){
        val checkActivationDataSource = CheckActivationDataSource(
                MeServiceFactory.getInstance().createRetrofitService(RecordsService::class.java, RecordsService.Service.SERVICE_ENDPOINT, accessToken))

        disposableHolder.add(checkActivationDataSource.checkActivation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen{throwables -> throwables.delay(CHECK_ACTIVATION_DELAY,TimeUnit.MILLISECONDS)}
                .repeatWhen{observable -> observable.delay(CHECK_ACTIVATION_DELAY, TimeUnit.MILLISECONDS)}
                .takeUntil{it == true}
                .subscribe { isActivated ->
                    if(isActivated) activationComplete.onNext(Unit)
                })

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