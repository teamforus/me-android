package io.forus.me.android.presentation.view.screens.records.categories

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class RecordCategoriesPresenter constructor(val recordsRepository: RecordsRepository) : LRPresenter<List<RecordCategory>, RecordCategoriesModel, RecordCategoriesView>() {


    override fun initialModelSingle(): Single<List<RecordCategory>> = Single.fromObservable(recordsRepository.getCategories())
            //.delay(1, TimeUnit.SECONDS)
            .map {
                it
            }


    override fun RecordCategoriesModel.changeInitialModel(i: List<RecordCategory>): RecordCategoriesModel = copy(items = i)


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
                RecordCategoriesModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordCategoriesView::render)

//        val observable = loadRefreshPartialChanges()
//        val initialViewState = LRViewState(false, null, false, false, null, MapModel("", "" ))
//        subscribeViewState(observable.scan(initialViewState, this::stateReducer).observeOn(AndroidSchedulers.mainThread()),MapView::render)
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