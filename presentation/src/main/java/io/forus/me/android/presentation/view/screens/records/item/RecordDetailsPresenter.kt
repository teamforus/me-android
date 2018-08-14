package io.forus.me.android.presentation.view.screens.records.item

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.presentation.helpers.QrCodeGenerator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RecordDetailsPresenter constructor(private val recordId: Long, private val recordsRepository: RecordsRepository) : LRPresenter<RecordDetailsModel, RecordDetailsModel, RecordDetailsView>() {


    override fun initialModelSingle(): Single<RecordDetailsModel> = Single.zip(
            Single.fromObservable(recordsRepository.getRecord(recordId)),
            Single.fromObservable(recordsRepository.getRecordUuid(recordId)),
            BiFunction { record : Record, uuid: String ->
                RecordDetailsModel(record, uuid)
            }
    )


    override fun RecordDetailsModel.changeInitialModel(i: RecordDetailsModel): RecordDetailsModel = copy(item = i.item, uuid = i.uuid).also {
        if(i.uuid != null && i.uuid.isNotEmpty()) {
            loadQrCode.onNext(i.uuid)
        }
    }

    private val loadQrCode = PublishSubject.create<String>()
    private fun loadQrCode(): Observable<String> = loadQrCode

    override fun bindIntents() {

        var observable = Observable.merge(

                loadRefreshPartialChanges(),
                intent { loadQrCode() }
                        .switchMap {
                            QrCodeGenerator.getRecordQrCode(it, 300, 300)
                                    .toObservable()
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map<PartialChange>{
                                        RecordDetailsPartialChanges.CreateQrCodeEnd(it)
                                    }
                                    .onErrorReturn {
                                        RecordDetailsPartialChanges.CreateQrCodeEnd(null)
                                    }
                                    .startWith(RecordDetailsPartialChanges.CreateQrCodeStart(it))
                        }
        )


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
            is RecordDetailsPartialChanges.CreateQrCodeStart -> vs.copy(model = vs.model.copy(creatingQrCode = true, qrCode = null))
            is RecordDetailsPartialChanges.CreateQrCodeEnd -> vs.copy(model = vs.model.copy(creatingQrCode = false, qrCode = change.bitmap))
        }

    }
}