package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.entity.records.request.NewRecordCategoryRequest
import io.forus.me.android.data.entity.records.request.CreateRecord
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordType
import io.reactivex.Observable
import io.reactivex.Single


class RecordsRemoteDataSource(private val recordsService: RecordsService): RecordsDataSource {




    override fun getRecordCategories(): Observable<List<RecordCategory>> = recordsService.listAllCategories().flatMap {
            if (it.isNotEmpty())
                Single.just(it.map { RecordCategory(it.id, it.name, it.order)}).toObservable()
            else {
                val general = io.forus.me.android.domain.models.records.NewRecordCategoryRequest("General", 0)
                val medical =  io.forus.me.android.domain.models.records.NewRecordCategoryRequest("Medical", 1)
                val personal =  io.forus.me.android.domain.models.records.NewRecordCategoryRequest("Personal", 2)
                Observable.concat(createCategory(general), createCategory(medical), createCategory(personal)).flatMap {
                    getRecordCategories()
                }
            }
        }

    override fun getRecordTypes(): Observable<List<RecordType>> = recordsService.listAllTypes().map {
        it.map { RecordType(it.key, it.type, it.name) }
    }

    override fun getRecords(type: RecordType): Observable<List<Record>> {
        return recordsService.listAllRecords(type.key);
    }

    override fun createRecord(model: NewRecordRequest): Observable<io.forus.me.android.data.entity.records.response.CreateRecordResult> {
        val createRecord = CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsService.createRecord(createRecord).map { io.forus.me.android.data.entity.records.response.CreateRecordResult(it.success) }
    }

    override fun createCategory(model: io.forus.me.android.domain.models.records.NewRecordCategoryRequest): Observable<Boolean> {
        return recordsService.createCategory(NewRecordCategoryRequest(model.name, model.order)).map {
            true
        }
    }


}