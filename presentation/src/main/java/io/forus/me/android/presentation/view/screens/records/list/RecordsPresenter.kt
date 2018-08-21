package io.forus.me.android.presentation.view.screens.records.list

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class RecordsPresenter constructor(private val recordCategoryId: Long, private val recordsRepository: RecordsRepository) : LRPresenter<List<Record>, RecordsModel, RecordsView>() {


    override fun initialModelSingle(): Single<List<Record>> =
            Single.fromObservable(recordsRepository.getRecords(recordCategoryId))


    override fun RecordsModel.changeInitialModel(i: List<Record>): RecordsModel = copy(items = i)


    override fun bindIntents() {

        var observable = loadRefreshPartialChanges();


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RecordsModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordsView::render)
    }

    override fun stateReducer(vs: LRViewState<RecordsModel>, change: PartialChange): LRViewState<RecordsModel> {

        if (change !is RecordsPartialChanges) return super.stateReducer(vs, change)

        return when (change) {

            else -> {
                super.stateReducer(vs, change)
            }
        }

    }
}