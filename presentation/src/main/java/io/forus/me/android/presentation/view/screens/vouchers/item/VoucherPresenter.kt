package io.forus.me.android.presentation.view.screens.vouchers.item

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class VoucherPresenter constructor(private val vouchersRepository: VouchersRepository, private val id: String) : LRPresenter<Voucher, VoucherModel, VoucherView>() {


    override fun initialModelSingle(): Single<Voucher> = Single.fromObservable(vouchersRepository.getVoucher(id))

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

    override fun stateReducer(viewState: LRViewState<VoucherModel>, change: PartialChange): LRViewState<VoucherModel> {

        if (change !is VoucherPartialChanges) return super.stateReducer(viewState, change)

        return run {
            super.stateReducer(viewState, change)
        }

    }











}