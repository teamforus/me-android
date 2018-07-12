package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.entity.records.request.CreateRecord
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.CreateRecordResult
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordType
import io.reactivex.Observable


class RecordsRemoteDataSource(private val recordsService: RecordsService): RecordsDataSource {


    override fun getRecordTypes(): Observable<List<RecordType>> = recordsService.listAllTypes().map {
        it.map { RecordType(it.key, it.type, it.name) }
    }

    override fun getRecordCategories(): Observable<List<RecordCategory>> = recordsService.listAllCategories().map {
        it.map { RecordCategory(it.id, it.name, it.order) }
    }

    override fun getRecords(type: RecordType): Observable<List<Record>> {
        return recordsService.listAllRecords(type.key);
    }

    override fun createRecord(model: NewRecordRequest): Observable<CreateRecordResult> {
        val createRecord = CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsService.createRecord(createRecord).map { CreateRecordResult(it.success) }
    }
}