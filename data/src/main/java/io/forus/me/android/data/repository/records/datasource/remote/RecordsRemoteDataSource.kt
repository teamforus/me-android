package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.entity.records.request.NewRecordCategoryRequest
import io.forus.me.android.data.entity.records.response.CreateRecordResult
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.reactivex.Observable
import io.reactivex.Single


public class RecordsRemoteDataSource(private val recordsService: RecordsService): RecordsDataSource {


    override fun getRecords(): Observable<List<RecordCategory>> = recordsService.getCategories().flatMap {
            if (it.isNotEmpty())
                Single.just(it.map { RecordCategory(it.id, it.name, it.order)}).toObservable()
            else {
                val general = io.forus.me.android.domain.models.records.NewRecordCategoryRequest("General", 0)
                val medical =  io.forus.me.android.domain.models.records.NewRecordCategoryRequest("Medical", 1)
                val personal =  io.forus.me.android.domain.models.records.NewRecordCategoryRequest("Personal", 2)
                Observable.concat(createCategory(general), createCategory(medical), createCategory(personal)).flatMap {
                    getRecords()
                }
            }
        }


    override fun createRecord(model: NewRecordRequest): Observable<CreateRecordResult> {
        return recordsService.createRecord()
    }

    override fun createCategory(model: io.forus.me.android.domain.models.records.NewRecordCategoryRequest): Observable<Boolean> {
        return recordsService.createCategory(NewRecordCategoryRequest(model.name, model.order)).map {
            true
        }
    }


}