package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.common.Success
import io.forus.me.android.data.entity.records.request.*
import io.forus.me.android.data.entity.records.response.Record
import io.forus.me.android.data.entity.records.response.RecordCategory
import io.forus.me.android.data.entity.records.response.RecordType
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.reactivex.Observable


class RecordsRemoteDataSource(private val recordsService: RecordsService): RecordsDataSource {

    override fun getRecordTypes(): Observable<List<RecordType>> = recordsService.listAllTypes()

    override fun getRecordCategories(): Observable<List<RecordCategory>> = recordsService.listAllCategories()

    override fun createRecordCategory(createCategory: CreateCategory): Observable<Success> = recordsService.createCategory(createCategory)

    override fun retrieveRecordCategory(id: Long) : Observable<RecordCategory> = recordsService.retrieveCategory(id)

    override fun updateRecordCategory(id: Long, updateCategory: UpdateCategory) : Observable<Success> = recordsService.updateCategory(id, updateCategory)

    override fun deleteRecordCategory(id: Long) : Observable<Success> = recordsService.deleteCategory(id)

    override fun sortRecordCategories(sortCategories: SortCategories) : Observable<Success> = recordsService.sortCategories(sortCategories)

    override fun getRecords(type: String): Observable<List<Record>> = recordsService.listAllRecords(type)

    override fun createRecord(createRecord: CreateRecord): Observable<Success> = recordsService.createRecord(createRecord.key, createRecord)

    override fun retrieveRecord(id: Long) : Observable<Record> = recordsService.retrieveRecord(id)

    override fun updateRecord(id: Long, updateRecord: UpdateRecord) : Observable<Success> = recordsService.updateRecord(id, updateRecord)

    override fun deleteRecord(id: Long) : Observable<Success> = recordsService.deleteRecord(id)

    override fun sortRecords(sortRecords: SortRecords) : Observable<Success> = recordsService.sortRecords(sortRecords)
}