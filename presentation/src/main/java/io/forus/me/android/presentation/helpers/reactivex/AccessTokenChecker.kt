package io.forus.me.android.presentation.helpers.reactivex

import io.forus.me.android.data.net.sign.SignService
import io.forus.me.android.data.net.MeServiceFactory
import io.forus.me.android.data.repository.account.datasource.remote.CheckActivationDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AccessTokenChecker(val serviceEndpoint: String){

    companion object {
        private const val CHECK_ACTIVATION_DELAY_MILLIS = 1000L
    }

    fun startCheckingActivation(accessToken: String, activationComplete: PublishSubject<Unit>): Disposable{
        val checkActivationDataSource = CheckActivationDataSource(MeServiceFactory.getInstance().createRetrofitService(SignService::class.java, serviceEndpoint))

        return checkActivationDataSource.checkActivation(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen{throwables -> throwables.delay(CHECK_ACTIVATION_DELAY_MILLIS, TimeUnit.MILLISECONDS)}
                .repeatWhen{observable -> observable.delay(CHECK_ACTIVATION_DELAY_MILLIS, TimeUnit.MILLISECONDS)}
                .takeUntil{it == true}
                .subscribe { isActivated ->
                    if(isActivated) activationComplete.onNext(Unit)
                }

    }
}