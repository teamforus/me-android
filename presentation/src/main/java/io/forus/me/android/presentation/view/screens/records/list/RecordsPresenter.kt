package io.forus.me.android.presentation.view.screens.records.list

import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsPartialChanges
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RecordsPresenter constructor(private val recordCategoryId: Long, private val recordsRepository: RecordsRepository) : LRPresenter<List<Record>, RecordsModel, RecordsView>() {


    override fun initialModelSingle(): Single<List<Record>> =
            Single.fromObservable(recordsRepository.getRecords())


    override fun RecordsModel.changeInitialModel(i: List<Record>): RecordsModel = copy(items = i)


    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.records() }
                        .switchMap { validatorId ->
                            recordsRepository.getRecords()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        RecordsPartialChanges.RequestRecordsSuccess(it)
                                    }
                                    .onErrorReturn {
                                        RecordsPartialChanges.RequestError(it)

                                    }
                        },

                intent { it.archives() }
                        .switchMap { validatorId ->
                            recordsRepository.getRecordsArchived()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        RecordsPartialChanges.RequestArchivesSuccess(it)
                                    }
                                    .onErrorReturn {
                                        RecordsPartialChanges.RequestError(it)

                                    }
                        }

        )


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RecordsModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordsView::render)
    }

    override fun stateReducer(vs: LRViewState<RecordsModel>, change: PartialChange): LRViewState<RecordsModel> {

        if (change !is RecordsPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RecordsPartialChanges.RequestRecordsSuccess -> vs.copy(
                    model = vs.model.copy(items = change.recordsR, requestError = null))
            is RecordsPartialChanges.RequestArchivesSuccess -> vs.copy(
                    model = vs.model.copy(archives = change.archiveR, requestError = null))
            is RecordsPartialChanges.RequestError -> vs.copy(
                    model = vs.model.copy(requestError = change.error))
        }
    }
}