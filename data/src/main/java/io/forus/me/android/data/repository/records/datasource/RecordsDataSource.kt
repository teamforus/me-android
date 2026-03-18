package io.forus.me.android.data.repository.records.datasource

import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.CreateCategory
import io.forus.me.android.data.entity.records.request.CreateRecord
import io.forus.me.android.data.entity.records.request.SortCategories
import io.forus.me.android.data.entity.records.request.SortRecords
import io.forus.me.android.data.entity.records.request.UpdateCategory
import io.forus.me.android.data.entity.records.request.UpdateRecord
import io.forus.me.android.data.entity.records.request.ValidateRecord
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.data.entity.records.response.RecordCategory
import io.forus.me.android.data.entity.records.response.RecordType
import io.forus.me.android.data.entity.records.response.Validation
import io.forus.me.android.data.entity.records.response.ValidationToken
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
