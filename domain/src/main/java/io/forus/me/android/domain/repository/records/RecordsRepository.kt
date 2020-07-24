package io.forus.me.android.domain.repository.records

import com.sun.net.httpserver.Authenticator
import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable

interface RecordsRepository {

    fun getRecordTypes(): Observable<List<RecordType>>


    fun getCategories(): Observable<List<RecordCategory>>


    fun getCategoriesWithRecordCount(): Observable<List<RecordCategory>>


    fun newCategory(newRecordCategoryRequest: NewRecordCategoryRequest): Observable<Boolean>


    fun getCategory(categoryId: Long?): Observable<RecordCategory>


    fun getRecordsCount(recordCategoryId: Long): Observable<Long>


    fun getRecords(): Observable<List<Record>>


    fun getRecordsArchived(): Observable<List<Record>>


    fun getRecords(recordCategoryId: Long): Observable<List<Record>>

    fun deleteRecord(id: Long) : Observable<Boolean>

   // fun updateRecord(id: Long, updateRecord: UpdateRecord) : Observable<Success>


    fun newRecord(model: NewRecordRequest): Observable<CreateRecordResponse>


    fun getRecord(recordId: Long): Observable<Record>


    fun getRecordUuid(recordId: Long): Observable<String>


    fun readValidation(uuid: String): Observable<Validation>


    fun approveValidation(uuid: String, organization_id: Long): Observable<Boolean>


    fun declineValidation(uuid: String): Observable<Boolean>
}
