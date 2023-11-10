package io.forus.me.android.domain.interactor

import io.forus.me.android.domain.executor.PostExecutionThread
import io.forus.me.android.domain.executor.ThreadExecutor
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.reactivex.Observable

class LoadVoucherUseCase(private val vouchersRepository: VouchersRepository, threadExecutor: ThreadExecutor?, postExecutionThread: PostExecutionThread?) : UseCase<Voucher, LoadVoucherUseCase.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Observable<Voucher> =
            vouchersRepository.getVoucher(params.address)


    class Params(var address: String) {

        companion object {
            @JvmStatic
            fun forVoucher(address: String) = Params(address)
        }
    }
}