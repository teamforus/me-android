package io.forus.me.android.data.repository.records.datasource

import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.domain.models.records.NewRecordCategoryRequest
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordType
import io.reactivex.Observable


interface RecordsDataSource {


    fun getRecordTypes(): Observable<List<RecordType>>


    fun getRecordCategories(): Observable<List<RecordCategory>>


    fun getRecords(type: RecordType): Observable<List<Record>>


    fun createRecord(model: NewRecordRequest): Observable<Boolean>


    fun createCategory(model: NewRecordCategoryRequest): Observable<Boolean>


}
