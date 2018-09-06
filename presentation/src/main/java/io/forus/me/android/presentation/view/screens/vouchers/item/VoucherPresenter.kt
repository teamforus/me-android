package io.forus.me.android.presentation.view.screens.vouchers.item

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class VoucherPresenter constructor(private val vouchersRepository: VouchersRepository, private val address: String) : LRPresenter<Voucher, VoucherModel, VoucherView>() {


    override fun initialModelSingle(): Single<Voucher> = Single.fromObservable(vouchersRepository.getVoucher(address))

    override fun VoucherModel.changeInitialModel(i: Voucher): VoucherModel = copy(item = i)

    override fun bindIntents() {

        val observable = loadRefreshPartialChanges();


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                VoucherModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                VoucherView::render)
    }

    override fun stateReducer(vs: LRViewState<VoucherModel>, change: PartialChange): LRViewState<VoucherModel> {

        if (change !is VoucherPartialChanges) return super.stateReducer(vs, change)

        return run {
            super.stateReducer(vs, change)
        }

    }
}