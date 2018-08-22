package io.forus.me.android.presentation.view.screens.account.restoreByEmail

import com.gigawatt.android.data.net.sign.RecordsService
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.repository.account.datasource.remote.CheckActivationDataSource
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.models.DisposableHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class RestoreByEmailPresenter constructor(private val disposableHolder: DisposableHolder, private val accountRepository: AccountRepository) : LRPresenter<Unit, RestoreByEmailModel, RestoreByEmailView>() {

    private val CHECK_ACTIVATION_DELAY = 1000L

    override fun initialModelSingle(): Single<Unit> = Single.just(Unit)


    override fun RestoreByEmailModel.changeInitialModel(i: Unit): RestoreByEmailModel = copy()

    private fun startCheckingActivation(accessToken: String){
        val checkActivationDataSource = CheckActivationDataSource(
                MeServiceFactory.getInstance().createRetrofitService(RecordsService::class.java, RecordsService.Service.SERVICE_ENDPOINT, accessToken))

        disposableHolder.add(checkActivationDataSource.checkActivation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen{throwables -> throwables.delay(CHECK_ACTIVATION_DELAY, TimeUnit.MILLISECONDS)}
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
                intent { activationComplete() }.map { RestoreByEmailPartialChanges.RestoreIdentity() },
                intent { it.register() }
                        .switchMap {
                            accountRepository.restoreByEmail(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        startCheckingActivation(it.accessToken)
                                        RestoreByEmailPartialChanges.RestoreByEmailRequestEnd(it)
                                    }
                                    .onErrorReturn {
                                        RestoreByEmailPartialChanges.RestoreByEmailRequestError(it)
                                    }
                                    .startWith(RestoreByEmailPartialChanges.RestoreByEmailRequestStart())

                        }
        );


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