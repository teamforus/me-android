package io.forus.me.android.presentation.view.screens.records.list

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class RecordsPresenter constructor(val recordsRepository: RecordsRepository) : LRPresenter<List<Record>, RecordsModel, RecordsView>() {


    override fun initialModelSingle(): Single<List<Record>> = Single.fromObservable(recordsRepository.getRecords())
            //.delay(1, TimeUnit.SECONDS)
            .map {
                it
            }


    override fun RecordsModel.changeInitialModel(i: List<Record>): RecordsModel = copy(items = i)


    override fun bindIntents() {

//        var observable = Observable.merge(
//
//                loadRefreshPartialChanges()
//        );

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

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(viewState: LRViewState<RecordsModel>, change: PartialChange): LRViewState<RecordsModel> {

        if (change !is RecordsPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }











}