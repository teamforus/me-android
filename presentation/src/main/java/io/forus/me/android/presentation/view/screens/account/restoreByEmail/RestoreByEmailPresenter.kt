package io.forus.me.android.presentation.view.screens.account.restoreByEmail

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPartialChange
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RestoreByEmailPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<RestoreAccountByEmailRequest, RestoreByEmailModel, RestoreByEmailView>() {


    override fun initialModelSingle(): Single<RestoreAccountByEmailRequest> = Single.just(RestoreAccountByEmailRequest())
            //.delay(1, TimeUnit.SECONDS)
            .flatMap {  Single.just(it)  }


    override fun RestoreByEmailModel.changeInitialModel(i: RestoreAccountByEmailRequest): RestoreByEmailModel = copy(item = i)


    override fun bindIntents() {

        var observable = Observable.merge(

                loadRefreshPartialChanges(),
                intent { it.register() }
                        .switchMap {
                            accountRepository.loginByEmail(it.email)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        RestoreByEmailPartialChanges.RegisterEnd(it)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                                    .startWith(RestoreByEmailPartialChanges.RegisterStart(it))

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

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<RestoreByEmailModel>, change: PartialChange): LRViewState<RestoreByEmailModel> {

        if (change !is RestoreByEmailPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {
            is RestoreByEmailPartialChanges.RegisterEnd -> viewState.copy(closeScreen = true, model = viewState.model.copy(sendingRegistration = false))
            is RestoreByEmailPartialChanges.RegisterStart -> viewState.copy(model = viewState.model.copy(sendingRegistration = true))

        }

    }











}