package io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment

import android.util.Log
import io.forus.me.android.data.repository.records.RecordsRepository
import io.forus.me.android.domain.models.records.CreateRecordResponse
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsPartialChanges
import io.forus.me.android.presentation.view.screens.records.newrecord.NewRecordPartialChanges
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateRecordModel(private val recordRepository: RecordsRepository) {


    fun createRecord(request: NewRecordRequest, success: (CreateRecordResponse) -> Unit, error: (Throwable) -> Unit) {
        Single.fromObservable(recordRepository.newRecord(request!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap<PartialChange> { createRecordResponse ->
                    success(createRecordResponse)
                    Observable.just(NewRecordPartialChanges.CreateRecordEnd(createRecordResponse))
                }
                .onErrorReturn {
                    error(it)
                    NewRecordPartialChanges.CreateRecordError(it)
                })
                .subscribe()
    }

    fun getRecordTypes(success: (CreateRecordResponse) -> Unit, error: (Throwable) -> Unit) {
        Single.fromObservable(recordRepository.getRecordTypes())
                .subscribe()

    }

    fun deleteRecord(id: Long, success: (Boolean) -> Unit, error: (Throwable) -> Unit) {
        Single.fromObservable(recordRepository.deleteRecord(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap<Boolean> {
                    success(it)
                    Observable.just(true)
                }
                .onErrorReturn {
                    error(it)
                    false
                }).subscribe()
    }
}