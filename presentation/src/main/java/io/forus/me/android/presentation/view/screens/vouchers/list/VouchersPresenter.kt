package io.forus.me.android.presentation.view.screens.vouchers.list

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class VouchersPresenter constructor(val vouchersRepository: VouchersRepository) : LRPresenter<List<Voucher>, VouchersModel, VouchersView>() {


    override fun initialModelSingle(): Single<List<Voucher>> = Single.fromObservable(vouchersRepository.getVouchers())

    override fun VouchersModel.changeInitialModel(i: List<Voucher>): VouchersModel {
        val vouchers: MutableList<Voucher> = mutableListOf()
        vouchers.addAll(i.filter { voucher ->  !(voucher.isProduct && voucher.isUsed)})
        vouchers.addAll(i.filter { voucher ->  (voucher.isProduct && voucher.isUsed)})
        return copy(items = vouchers)
    }


    override fun bindIntents() {
        val observable = loadRefreshPartialChanges()


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                VouchersModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                VouchersView::render)

    }

    override fun stateReducer(viewState: LRViewState<VouchersModel>, change: PartialChange): LRViewState<VouchersModel> {

        if (change !is VouchersPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}