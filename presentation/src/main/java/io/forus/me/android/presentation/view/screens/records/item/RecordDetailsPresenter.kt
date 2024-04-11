package io.forus.me.android.presentation.view.screens.records.item

import android.content.Context
import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.validators.ValidatorsRepository
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.records.item.validations.ValidationViewModel
import io.forus.me.android.presentation.view.screens.records.item.validators.ValidatorViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecordDetailsPresenter constructor(private val context: Context, private val recordId: Long, private val recordsRepository: RecordsRepository, private val validatorsRepository: ValidatorsRepository) : LRPresenter<RecordDetailsModel, RecordDetailsModel, RecordDetailsView>() {


    override fun initialModelSingle(): Single<RecordDetailsModel> = Single.fromObservable(
            recordsRepository.getRecord(recordId).map {


                val allValidations = mutableListOf<ValidationViewModel>()
                if (it.validations.isNotEmpty()) {
                    allValidations.add(ValidationViewModel(context.getString(R.string.validations)))
                    allValidations.addAll(it.validations.map { ValidationViewModel(it) })

                }

                if (allValidations.isEmpty()) allValidations.add(ValidationViewModel(context.getString(R.string.validations_empty)))


                RecordDetailsModel(item = it, validations = allValidations)
            }
    )


    override fun RecordDetailsModel.changeInitialModel(i: RecordDetailsModel): RecordDetailsModel = copy(item = i.item, validations = i.validations, requestValidationError = null)

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { it.requestValidation() }
                        .switchMap { validatorId ->
                            validatorsRepository.requestValidation(recordId, validatorId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange> {
                                        RecordDetailsPartialChanges.RequestValidationEnd(validatorId)
                                    }
                                    .onErrorReturn {
                                        RecordDetailsPartialChanges.RequestValidationError(it)
                                    }
                                    .startWith(RecordDetailsPartialChanges.RequestValidationStart(validatorId))
                        },

                intent(RecordDetailsView::deleteRecord).switchMap {
                    recordsRepository.deleteRecord(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map<PartialChange> {
                                RecordDetailsPartialChanges.DeleteRecordSuccess(it)
                            }
                            .onErrorReturn {
                                RecordDetailsPartialChanges.DeleteRecordError(it)
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
                RecordDetailsModel(),
                false)

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordDetailsView::render)
    }

    override fun stateReducer(vs: LRViewState<RecordDetailsModel>, change: PartialChange): LRViewState<RecordDetailsModel> {

        if (change !is RecordDetailsPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RecordDetailsPartialChanges.RequestValidationStart -> vs.copy(model = vs.model.copy(requestValidationError = null))
            is RecordDetailsPartialChanges.RequestValidationEnd -> vs.copy(model = vs.model.changeStatus(change.validatorId))
            is RecordDetailsPartialChanges.RequestValidationError -> vs.copy(model = vs.model.copy(requestValidationError = change.error))
            is RecordDetailsPartialChanges.DeleteRecordSuccess -> vs.copy(model = vs.model.copy(recordDeleteSuccess = change.success))
            is RecordDetailsPartialChanges.DeleteRecordError -> vs.copy(model = vs.model.copy(recordDeleteError = change.error))
        }
    }
}