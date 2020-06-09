package io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment

import android.util.Log
import io.forus.me.android.data.repository.records.RecordsRepository
import io.forus.me.android.domain.models.records.CreateRecordResponse
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.records.newrecord.NewRecordPartialChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateRecordModel(private val recordRepository: RecordsRepository) {



    public fun createRecord(request: NewRecordRequest, success: (CreateRecordResponse)->Unit, error: (Throwable) -> Unit ){





        Log.d("forus","createRecord")
        Observable.just(recordRepository.newRecord(request!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap<PartialChange> { createRecordResponse ->
                    Log.d("forus","success ${createRecordResponse.javaClass}")
                   success(createRecordResponse)
                    Observable.just(NewRecordPartialChanges.CreateRecordEnd(createRecordResponse))


                }
                .onErrorReturn {
                    Log.d("forus","error")
                    error(it)
                    NewRecordPartialChanges.CreateRecordError(it)
                }
               .subscribe())
    }
}