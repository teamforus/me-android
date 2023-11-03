package io.forus.me.android.domain.interactor

import io.forus.me.android.domain.executor.PostExecutionThread
import io.forus.me.android.domain.executor.ThreadExecutor
import io.forus.me.android.domain.models.account.Account
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable

class LoadAccountUseCase(threadExecutor: ThreadExecutor,
                         postExecutionThread: PostExecutionThread,
                         private val accountRepository: AccountRepository
) : UseCase<Account, LoadAccountUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params?): Observable<Account> =
            accountRepository.getAccount()


    class Params
}
