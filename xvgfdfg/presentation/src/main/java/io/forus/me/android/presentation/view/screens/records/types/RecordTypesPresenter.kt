package io.forus.me.android.presentation.view.screens.records.types

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class RecordTypesPresenter constructor(val recordsRepository: RecordsRepository) : LRPresenter<List<io.forus.me.android.domain.models.records.RecordType>, RecordTypesModel, RecordTypesView>() {


    override fun initialModelSingle(): Single<List<io.forus.me.android.domain.models.records.RecordType>> = Single.fromObservable(
              recordsRepository.getRecordTypes()
    )

    override fun RecordTypesModel.changeInitialModel(i: List<io.forus.me.android.domain.models.records.RecordType>): RecordTypesModel = copy(items = i)


    override fun bindIntents() {
        val observable = loadRefreshPartialChanges()


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RecordTypesModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordTypesView::render)
    }

    override fun stateReducer(viewState: LRViewState<RecordTypesModel>, change: PartialChange): LRViewState<RecordTypesModel> {

        if (change !is RecordTypesPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}