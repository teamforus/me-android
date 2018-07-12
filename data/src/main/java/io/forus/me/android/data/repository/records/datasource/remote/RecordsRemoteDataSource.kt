package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.entity.records.response.CreateRecordResult
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.reactivex.Observable


public class RecordsRemoteDataSource(private val recordsService: RecordsService): RecordsDataSource {


    override fun getRecords(): Observable<List<RecordCategory>> = recordsService.getCategories().map {
        it.map { RecordCategory(it.id, it.name, it.order) }
    }

    override fun createRecord(model: NewRecordRequest): Observable<CreateRecordResult> {
        return recordsService.createRecord()
    }
}