package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.domain.repository.validators.ValidatorsRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

class NewRecordPresenter constructor(private val recordRepository: RecordsRepository, private val validatorsRepository: ValidatorsRepository) : LRPresenter<NewRecordModel, NewRecordModel, NewRecordView>() {

    var request: NewRecordRequest? = null;

    override fun initialModelSingle(): Single<NewRecordModel> = Single.zip(
            Single.fromObservable(recordRepository.getRecordTypes()),
            Single.fromObservable(recordRepository.getCategories()),
            Single.fromObservable(validatorsRepository.getValidators()),
            Function3 { types : List<RecordType>, categories: List<RecordCategory>, validators: List<SimpleValidator> ->
                NewRecordModel(types = types, categories = categories, validators = validators)
            }
    )


    override fun NewRecordModel.changeInitialModel(i: NewRecordModel): NewRecordModel = i.copy()

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),
                Observable.mergeArray(
                        intent { it.selectCategory() }
                                .map {  NewRecordPartialChanges.SelectCategory(it) },
                        intent { it.selectType() }
                                .map {  NewRecordPartialChanges.SelectType(it) },
                        intent { it.setValue() }
                                .map {  NewRecordPartialChanges.SetValue(it) },
                        intent { it.selectValidator()}
                                .map {  NewRecordPartialChanges.SelectValidator(it) },
                        intent { it.previousStep()}
                                .map {  NewRecordPartialChanges.PreviousStep() },
                        intent { it.nextStep() }
                                .map {  NewRecordPartialChanges.NextStep() },

                        intent { it.submit() }
                                .switchMap {
                                    recordRepository.newRecord(request!!)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .switchMap<PartialChange> { createRecordResponse ->
                                                validatorsRepository.requestValidations(createRecordResponse.id, request!!.validators.map { it.id })
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .map {
                                                            NewRecordPartialChanges.CreateRecordEnd(createRecordResponse)
                                                        }

                                            }
                                            .onErrorReturn {
                                                NewRecordPartialChanges.CreateRecordError(it)
                                            }
                                            .startWith(NewRecordPartialChanges.CreateRecordStart(request!!))
                                }

                )
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

    }

    override fun stateReducer(vs: LRViewState<NewRecordModel>, change: PartialChange): LRViewState<NewRecordModel> {
        var result : LRViewState<NewRecordModel>? = null
        if (change !is NewRecordPartialChanges)
            result =  super.stateReducer(vs, change)

        when (change) {
            is NewRecordPartialChanges.CreateRecordEnd -> result = vs.copy(closeScreen = true, model = vs.model.copy(sendingCreateRecord = false, sendingCreateRecordError = null))
            is NewRecordPartialChanges.CreateRecordStart -> result = vs.copy(model = vs.model.copy(sendingCreateRecord = true, sendingCreateRecordError = null))
            is NewRecordPartialChanges.CreateRecordError -> result = vs.copy(model = vs.model.copy(sendingCreateRecord = false, sendingCreateRecordError = change.error))
            is NewRecordPartialChanges.SelectCategory -> result = vs.copy(model = vs.model.copy(item = vs.model.item.copy(category = change.category)))
            is NewRecordPartialChanges.SelectType -> result = vs.copy(model = vs.model.copy(item = vs.model.item.copy(recordType = change.type)))
            is NewRecordPartialChanges.SetValue -> result = vs.copy(model = vs.model.copy(item = vs.model.item.copy(value = change.value)))
            is NewRecordPartialChanges.SelectValidator -> result = vs.copy(model = vs.model.copy(item = vs.model.item.selectValidator(change.validator)))
            is NewRecordPartialChanges.PreviousStep -> result = vs.copy(model = vs.model.copy(currentStep = vs.model.currentStep - (if (vs.model.currentStep < NewRecordView.NUM_PAGES && vs.model.currentStep > 0) 1 else 0)))
            is NewRecordPartialChanges.NextStep -> result = vs.copy(model = vs.model.copy(currentStep = vs.model.currentStep + (if (vs.model.currentStep < NewRecordView.NUM_PAGES - 1 && vs.model.currentStep >= 0) 1 else 0)))
        }


        request = result?.model?.item

        return  result!!


    }
}