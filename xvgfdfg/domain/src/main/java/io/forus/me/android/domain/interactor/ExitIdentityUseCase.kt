package io.forus.me.android.domain.interactor

import io.forus.me.android.domain.executor.PostExecutionThread
import io.forus.me.android.domain.executor.ThreadExecutor
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable

class ExitIdentityUseCase(private val accountRepository: AccountRepository,
                          threadExecutor: ThreadExecutor?,
                          postExecutionThread: PostExecutionThread?): UseCase<Boolean, Unit>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Unit?): Observable<Boolean> {
        return accountRepository.exitIdentity()
    }
}