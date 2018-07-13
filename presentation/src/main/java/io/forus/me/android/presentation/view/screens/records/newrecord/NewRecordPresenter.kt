package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPartialChange
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewRecordPresenter constructor(private val recordRepository: RecordsRepository) : LRPresenter<NewRecordModel, NewRecordModel, NewRecordView>() {


    //TODO MAKE IT PARALLELS
    override fun initialModelSingle(): Single<NewRecordModel> = Single.fromObservable(
            recordRepository.getRecordTypes().flatMap {
                val types = it
                recordRepository.getCategories().map {
                    NewRecordModel(types = types, categories = it)
                }
            })


    override fun NewRecordModel.changeInitialModel(i: NewRecordModel): NewRecordModel = i.copy()


    override fun bindIntents() {

        var observable = Observable.merge(

                loadRefreshPartialChanges(),
                intent { it.createRecord() }
                        .switchMap {
                            recordRepository.newRecord(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        NewRecordPartialChanges.CreateRecordEnd(it)
                                    }
                                    .onErrorReturn {
                                        LRPartialChange.LoadingError(it)
                                    }
                                    .startWith(NewRecordPartialChanges.CreateRecordStart(it))
                        }
        );


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                NewRecordModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                NewRecordView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
    }

    override fun stateReducer(vs: LRViewState<NewRecordModel>, change: PartialChange): LRViewState<NewRecordModel> {

        if (change !is NewRecordPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is NewRecordPartialChanges.CreateRecordEnd -> vs.copy(closeScreen = true, model = vs.model.copy(sendingCreateRecord = false))
            is NewRecordPartialChanges.CreateRecordStart -> vs.copy(model = vs.model.copy(sendingCreateRecord = true))

        }

    }
}