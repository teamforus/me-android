package io.forus.me.android.data.repository.records.datasource

import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.*

import io.reactivex.Observable


interface RecordsDataSource {


    fun getRecordTypes(): Observable<List<RecordType>>


    fun getRecordCategories(): Observable<List<RecordCategory>>


    fun createRecordCategory(createCategory: CreateCategory): Observable<Success>


    fun retrieveRecordCategory(id: Long) : Observable<RecordCategory>


    fun updateRecordCategory(id: Long, updateCategory: UpdateCategory) : Observable<Success>


    fun deleteRecordCategory(id: Long) : Observable<Success>


    fun sortRecordCategories(sortCategories: SortCategories) : Observable<Success>


    fun getRecords(categoryId: Long): Observable<List<Record>>


    fun getRecords(): Observable<List<Record>>


    fun getRecordsArchived(): Observable<List<Record>>


    fun getRecords(type: String): Observable<List<Record>>


    fun createRecord(createRecord: CreateRecord): Observable<Record>


    fun retrieveRecord(id: Long) : Observable<Record>


    fun updateRecord(id: Long, updateRecord: UpdateRecord) : Observable<Success>




    fun deleteRecord(id: Long) : Observable<Success>


    fun sortRecords(sortRecords: SortRecords) : Observable<Success>


    fun createValidationToken(recordId: Long): Observable<ValidationToken>


    fun readValidation(uuid: String): Observable<Validation>


    fun approveValidation(uuid: String,  validateRecord: ValidateRecord): Observable<Success>


    fun declineValidation(uuid: String): Observable<Success>
}
