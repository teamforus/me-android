package io.forus.me.android.presentation.view.screens.records.categories

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class RecordCategoriesPresenter constructor(val recordsRepository: RecordsRepository) : LRPresenter<List<Record>, RecordCategoriesModel, RecordCategoriesView>() {


    override fun initialModelSingle(): Single<List<Record>> = Single.fromObservable(
            //recordsRepository.getCategoriesWithRecordCount()
            recordsRepository.getRecords()
    )

    override fun RecordCategoriesModel.changeInitialModel(i: List<Record>): RecordCategoriesModel = copy(items = i)


    override fun bindIntents() {
        val observable = loadRefreshPartialChanges()


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RecordCategoriesModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordCategoriesView::render)
    }

    override fun stateReducer(viewState: LRViewState<RecordCategoriesModel>, change: PartialChange): LRViewState<RecordCategoriesModel> {

        if (change !is RecordCategoriesPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}