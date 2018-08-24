package io.forus.me.android.presentation.view.screens.records.item

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.models.records.Validator
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.records.ValidationRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class RecordDetailsPresenter constructor(private val recordId: Long, private val recordsRepository: RecordsRepository, private val validationRepository: ValidationRepository) : LRPresenter<RecordDetailsModel, RecordDetailsModel, RecordDetailsView>() {


    override fun initialModelSingle(): Single<RecordDetailsModel> = Single.zip(
            Single.fromObservable(recordsRepository.getRecord(recordId)),
            Single.fromObservable(validationRepository.getValidators()),
            BiFunction { record : Record, validators: List<Validator> ->
                val allValidations = mutableListOf<Validator>()
                allValidations.addAll(record.validations.map { Validator(-1, it.identityAddress, "", Validation.p2pIcon, Validator.Status.approved) }.distinctBy { it.name })
                allValidations.addAll(validators)
                RecordDetailsModel(item = record, validators = allValidations)
            }
    )


    override fun RecordDetailsModel.changeInitialModel(i: RecordDetailsModel): RecordDetailsModel = copy(item = i.item, validators = i.validators)

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