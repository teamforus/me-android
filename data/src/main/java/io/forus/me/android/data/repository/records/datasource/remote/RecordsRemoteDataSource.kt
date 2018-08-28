package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.*
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.reactivex.Observable


class RecordsRemoteDataSource(f: () -> RecordsService): RecordsDataSource, RemoteDataSource<RecordsService>(f) {

    override fun getRecordTypes(): Observable<List<RecordType>> = service.listAllTypes()

    override fun getRecordCategories(): Observable<List<RecordCategory>> = service.listAllCategories()

    override fun createRecordCategory(createCategory: CreateCategory): Observable<Success> = service.createCategory(createCategory)

    override fun retrieveRecordCategory(id: Long) : Observable<RecordCategory> = service.retrieveCategory(id)

    override fun updateRecordCategory(id: Long, updateCategory: UpdateCategory) : Observable<Success> = service.updateCategory(id, updateCategory)

    override fun deleteRecordCategory(id: Long) : Observable<Success> = service.deleteCategory(id)

    override fun sortRecordCategories(sortCategories: SortCategories) : Observable<Success> = service.sortCategories(sortCategories)

    override fun getRecords(categoryId: Long): Observable<List<Record>> = service.listAllRecords(null, categoryId)

    override fun getRecords(type: String): Observable<List<Record>> = service.listAllRecords(type, null)

    override fun createRecord(createRecord: CreateRecord): Observable<Record> = service.createRecord(createRecord.key, createRecord)

    override fun retrieveRecord(id: Long) : Observable<Record> = service.retrieveRecord(id)

    override fun updateRecord(id: Long, updateRecord: UpdateRecord) : Observable<Success> = service.updateRecord(id, updateRecord)

    override fun deleteRecord(id: Long) : Observable<Success> = service.deleteRecord(id)

    override fun sortRecords(sortRecords: SortRecords) : Observable<Success> = service.sortRecords(sortRecords)

    override fun createValidationToken(recordId: Long): Observable<ValidationToken> = service.createValidationToken(CreateValidationToken(recordId))

    override fun readValidation(uuid: String): Observable<Validation> = service.readValidation(uuid)

    override fun approveValidation(uuid: String): Observable<Success> = service.approveValidation(uuid)

    override fun declineValidation(uuid: String): Observable<Success> = service.declineValidation(uuid)
}