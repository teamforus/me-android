package io.forus.me.android.domain.interactor

import io.forus.me.android.domain.executor.PostExecutionThread
import io.forus.me.android.domain.executor.ThreadExecutor
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.reactivex.Observable

class SendEmailUseCase(private val vouchersRepository: VouchersRepository,
                       threadExecutor: ThreadExecutor?,
                       postExecutionThread: PostExecutionThread?
) : UseCase<Boolean, SendEmailUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Observable<Boolean> =
            vouchersRepository.sendEmail(params.address)


    class Params(var address: String) {

        companion object {
            @JvmStatic
            fun forVoucher(address: String) = Params(address)
        }
    }
}