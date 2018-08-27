package io.forus.me.android.presentation.view.screens.records.item.qr

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRPresenter
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.domain.repository.records.RecordsRepository
import io.forus.me.android.presentation.helpers.reactivex.DisposableHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RecordQRPresenter constructor(private val recordId: Long, private val disposableHolder: DisposableHolder, private val recordsRepository: RecordsRepository) : LRPresenter<String, RecordQRModel, RecordQRView>() {

    override fun initialModelSingle(): Single<String> = Single.fromObservable(recordsRepository.getRecordUuid(recordId))

    override fun RecordQRModel.changeInitialModel(i: String): RecordQRModel = copy(uuid = i).also {
        disposableHolder.add(recordsRepository.readValidation(i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen{throwables -> throwables.delay(1000, TimeUnit.MILLISECONDS)}
                .repeatWhen{observable -> observable.delay(1000, TimeUnit.MILLISECONDS)}
                .takeUntil{it.state != Validation.State.pending}
                .subscribe {
                    if(it.state != Validation.State.pending) validationComplete.onNext(Unit)
                })
    }

    private val validationComplete = PublishSubject.create<Unit>()
    fun validationComplete(): Observable<Unit> = validationComplete

    override fun bindIntents() {

        val observable = Observable.merge(

                loadRefreshPartialChanges(),

                intent { validationComplete() }.map { RecordQRPartialChanges.RecordValidated() }
        )

        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                RecordQRModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                RecordQRView::render)
    }

    override fun stateReducer(vs: LRViewState<RecordQRModel>, change: PartialChange): LRViewState<RecordQRModel> {

        if (change !is RecordQRPartialChanges) return super.stateReducer(vs, change)

        return when (change) {
            is RecordQRPartialChanges.RecordValidated -> vs.copy(closeScreen = true, model = vs.model.copy(isRecordValidated = true))
        }

    }
}