package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.validators.ValidatorsRepository
import io.forus.me.android.presentation.view.screens.records.item.validations.ValidationViewModel
import io.forus.me.android.presentation.view.screens.records.item.validators.ValidatorViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecordDetailsPresenter constructor(private val recordId: Long, private val recordsRepository: RecordsRepository, private val validatorsRepository: ValidatorsRepository) : LRPresenter<RecordDetailsModel, RecordDetailsModel, RecordDetailsView>() {


    override fun initialModelSingle(): Single<RecordDetailsModel> = Single.fromObservable(
            //recordsRepository.getCategoriesWithRecordCount()
            recordsRepository.getRecord(recordId).map {


                val allValidations = mutableListOf<ValidationViewModel>()
                if (it.validations.isNotEmpty()) {
                    allValidations.add(ValidationViewModel("Validations"))
                    allValidations.addAll(it.validations.map { ValidationViewModel(it) })

                }

                if (allValidations.isEmpty()) allValidations.add(ValidationViewModel("You have no validations yet"))


                RecordDetailsModel(item = it, validations = allValidations)
            }
    )
    /*Single.zip(
            Single.fromObservable(recordsRepository.getRecord(recordId)),
            Single.fromObservable(validatorsRepository.getValidators()),
            Single.fromObservable(validatorsRepository.getValidators(recordId)),
            Function3 { record : Record, validators: List<SimpleValidator>, activeValidators: List<SimpleValidator> ->

                val allValidations = mutableListOf<ValidatorViewModel>()
                val p2pValidations = mutableListOf<ValidatorViewModel>()
                val possibleValidators = mutableListOf<ValidatorViewModel>()

                record.validations.distinctBy { it.identityAddress }.forEach{
                    p2pValidations.add(ValidatorViewModel(-1, it.identityAddress ?: "?", "Peer-to-peer validation", Validation.p2pIcon))
                }

                validators.forEach{
                    var requestExists = false
                    for(v in activeValidators){
                        if(v.id == it.id && v.organizationId == it.organizationId){
                            requestExists = true
                            break
                        }
                    }
                    if(!requestExists) possibleValidators.add(ValidatorViewModel(it))
                }

                if(activeValidators.isNotEmpty() || possibleValidators.isNotEmpty()){
                    allValidations.add(ValidatorViewModel("Validators"))
                    allValidations.addAll(activeValidators.map { ValidatorViewModel(it) })
                    allValidations.addAll(possibleValidators)
                }

                if(p2pValidations.isNotEmpty()){
                    allValidations.add(ValidatorViewModel("Peer-to-peer validations"))
                    allValidations.addAll(p2pValidations)
                }

                if(allValidations.isEmpty()) allValidations.add(ValidatorViewModel("You have no validations yet"))

                RecordDetailsModel(item = record, validators = allValidations)
            }
    )*/


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
        }
    }
}