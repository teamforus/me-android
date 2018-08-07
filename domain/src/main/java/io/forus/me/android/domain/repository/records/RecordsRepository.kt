package io.forus.me.android.domain.repository.records

import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable

interface RecordsRepository {

    fun getRecordTypes(): Observable<List<RecordType>>


    fun getCategories(): Observable<List<RecordCategory>>


    fun newCategory(model: NewRecordCategoryRequest): Observable<Boolean>


    fun getCategory(id: Long): Observable<RecordCategory>


    fun getRecords(recordCategory: RecordCategory): Observable<List<Record>>


    fun newRecord(model: NewRecordRequest) : Observable<NewRecordRequest>


    fun getRecord(id: Long): Observable<Record>
}
