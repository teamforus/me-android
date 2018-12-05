package io.forus.me.android.presentation.view.screens.dashboard

import io.forus.me.android.domain.interactor.CheckLoginUseCase
import io.forus.me.android.domain.interactor.UseCase
import io.forus.me.android.domain.interactor.DefaultObserver
import io.forus.me.android.domain.interactor.LoadAccountUseCase
import io.forus.me.android.domain.models.account.Account


class DashboardPresenter(private var view: DashboardContract.View?,
                         private val checkLoginUseCase: UseCase<Boolean, CheckLoginUseCase.Params>,
                         private val loadAccountUseCase: UseCase<Account, LoadAccountUseCase.Params>): DashboardContract.Presenter {


    override fun onCreate() {
        checkLoginUseCase.execute(CheckLoginObserver(),
                CheckLoginUseCase.Params())
    }

    override fun onDestroy() {
        view = null
    }

    private inner class CheckLoginObserver : DefaultObserver<Boolean>() {

        override fun onNext(isLoggined: Boolean) {
            if (isLoggined) {
                this@DashboardPresenter.view?.logout()
            } else {
                loadAccountUseCase.execute(LoadAccountObserver(), LoadAccountUseCase.Params())
            }
        }
    }

    private inner class LoadAccountObserver : DefaultObserver<Account>() {
        override fun onNext(account: Account) {
            this@DashboardPresenter.view?.addUserId(account.address)
        }
    }
}