package io.forus.me.android.presentation.view.screens.records.item

import android.graphics.Bitmap
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
            QrCodeGenerator.getRecordQrCode(i.uuid, 300, 300)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { t: Bitmap? -> if (t != null) qrCodeLoaded.onNext(t) }
        }
    }

    private val qrCodeLoaded = PublishSubject.create<Bitmap>()
    private fun qrCodeLoaded(): Observable<Bitmap> = qrCodeLoaded

    override fun bindIntents() {

        var observable = Observable.merge(
                loadRefreshPartialChanges(),
                intent { qrCodeLoaded() }
                        .map { RecordDetailsPartialChanges.QrCodeLoaded(it) }
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
            is RecordDetailsPartialChanges.QrCodeLoaded -> vs.copy(model = vs.model.copy(qrCode = change.bitmap))
        }

    }
}