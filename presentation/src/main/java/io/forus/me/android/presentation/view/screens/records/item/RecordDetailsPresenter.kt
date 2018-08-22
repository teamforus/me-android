package io.forus.me.android.presentation.view.screens.records.item

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.Validator
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.records.ValidationRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3

class RecordDetailsPresenter constructor(private val recordId: Long, private val recordsRepository: RecordsRepository, private val validationRepository: ValidationRepository) : LRPresenter<RecordDetailsModel, RecordDetailsModel, RecordDetailsView>() {


    override fun initialModelSingle(): Single<RecordDetailsModel> = Single.zip(
            Single.fromObservable(recordsRepository.getRecord(recordId)),
            Single.fromObservable(recordsRepository.getRecordUuid(recordId)),
            Single.fromObservable(validationRepository.getValidators()),
            Function3 { record : Record, uuid: String, validators: List<Validator> ->
                RecordDetailsModel(item = record, uuid = uuid, validators = validators)
            }
    )


    override fun RecordDetailsModel.changeInitialModel(i: RecordDetailsModel): RecordDetailsModel = copy(item = i.item, uuid = i.uuid, validators = i.validators)

    override fun bindIntents() {

        val observable = loadRefreshPartialChanges()

        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RecordDetailsModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordDetailsView::render)
    }

    override fun stateReducer(vs: LRViewState<RecordDetailsModel>, change: PartialChange): LRViewState<RecordDetailsModel> {

        if (change !is RecordDetailsPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            else -> super.stateReducer(vs, change)
        }
    }
}