package io.forus.me.android.presentation.view.screens.account.send_crash_reports

import android.util.Log
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.base.lr.LRPartialChange
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.account.account.AccountPartialChanges
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * Created by maestrovs on 23.04.2020.
 */
class SendReportsPresenter constructor(private val accountRepository: AccountRepository) :
        LRPresenter<SendReportsModel, SendReportsModel, SendReportsView>() {


    override fun initialModelSingle(): Single<SendReportsModel> = Single.zip(
            Single.fromObservable(accountRepository.getAccount()),
            Single.fromObservable(accountRepository.getSecurityOptions()),
            BiFunction { account, securityOptions ->
                SendReportsModel(false)
            })

    override fun SendReportsModel.changeInitialModel(i: SendReportsModel): SendReportsModel {
        return i.copy()
    }


    override fun bindIntents() {


        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.switchSendCrashReports() }
                        .switchMap { newState ->
                            accountRepository.setSendCrashReportsEnabled(newState)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> { success ->
                                        SendReportsPartialChanges.SendCrashReportsEnabled(if (success) newState else !newState)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                        }
        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                SendReportsModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                SendReportsView::render)
    }

    override fun stateReducer(vs: LRViewState<SendReportsModel>, change: PartialChange): LRViewState<SendReportsModel> {

        if (change !is SendReportsPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is SendReportsPartialChanges.SendCrashReportsEnabled -> vs.copy(model = vs.model.copy(sendCrashReportsEnabled = change.value))
        }
    }
}