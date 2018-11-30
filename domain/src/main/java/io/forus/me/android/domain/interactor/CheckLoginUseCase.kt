package io.forus.me.android.domain.interactor

import io.forus.me.android.domain.executor.PostExecutionThread
import io.forus.me.android.domain.executor.ThreadExecutor
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable

class CheckLoginUseCase(val accountRepository: AccountRepository,
                        threadExecutor: ThreadExecutor?,
                        postExecutionThread: PostExecutionThread?)
    : UseCase<Boolean, CheckLoginUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params?): Observable<Boolean> {
        return accountRepository.checkCurrentToken()
    }

    class Params private constructor(private val userId: Int) {
        companion object {

            fun forUser(userId: Int): Params {
                return Params(userId)
            }
        }
    }
}