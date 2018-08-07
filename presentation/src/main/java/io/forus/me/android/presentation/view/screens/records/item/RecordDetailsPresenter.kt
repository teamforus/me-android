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
import io.reactivex.subjects.PublishSubject

class RecordDetailsPresenter constructor(private val recordId: Long, private val recordsRepository: RecordsRepository) : LRPresenter<Record, RecordDetailsModel, RecordDetailsView>() {


    override fun initialModelSingle(): Single<Record> =
            Single.fromObservable(recordsRepository.getRecord(recordId))


    override fun RecordDetailsModel.changeInitialModel(i: Record): RecordDetailsModel = copy(item = i).also {
        val uuid: String = "9f60767cb70abc69cc577e4bbc7f7423aff0098aad6751e42f1ba81b5dd8dcfa"
        QrCodeGenerator.getRecordQrCode(uuid, 240, 240).subscribe{t: Bitmap? ->  if(t!= null) qrCodeLoaded.onNext(t)}
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
            is RecordDetailsPartialChanges.QrCodeLoaded -> vs.copy(closeScreen = true, model = vs.model.copy(qrCode = change.bitmap))
        }

    }
}