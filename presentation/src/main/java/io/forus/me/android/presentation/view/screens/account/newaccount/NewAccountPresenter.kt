package io.forus.me.android.presentation.view.screens.account.newaccount

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPartialChange
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.screens.account.newaccount.NewAccountModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NewAccountPresenter constructor(private val accountRepository: AccountRepository) : LRPresenter<NewAccountRequest, NewAccountModel, NewAccountView>() {


    override fun initialModelSingle(): Single<NewAccountRequest> = Single.just(NewAccountRequest())
            //.delay(1, TimeUnit.SECONDS)
            .flatMap {  Single.just(it)  }


    override fun NewAccountModel.changeInitialModel(i: NewAccountRequest): NewAccountModel = copy(item = i)


    override fun bindIntents() {

        var observable = Observable.merge(

                loadRefreshPartialChanges(),
                intent { it.register() }
                        .switchMap {
                            accountRepository.newUser(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        NewAccountPartialChanges.RegisterEnd(it)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                                    .startWith(NewAccountPartialChanges.RegisterStart(it))

                        }
        );


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                NewAccountModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                NewAccountView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<NewAccountModel>, change: PartialChange): LRViewState<NewAccountModel> {

        if (change !is NewAccountPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {
            is NewAccountPartialChanges.RegisterEnd -> viewState.copy(closeScreen = true, model = viewState.model.copy(sendingRegistration = false))
            is NewAccountPartialChanges.RegisterStart -> viewState.copy(model = viewState.model.copy(sendingRegistration = true))

        }

    }











}