package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.data.repository.records.datasource.mock.RecordsMockDataSource
import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class RecordsRepository(private val recordsMockDataSource: RecordsMockDataSource, private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

    override fun getRecordTypes(): Observable<List<RecordType>> {
        return recordsRemoteDataSource.getRecordTypes()
                .map { it.map { RecordType(it.key, it.type, it.name) }}
    }

    override fun getCategories(): Observable<List<RecordCategory>> {
        return recordsMockDataSource.getRecordCategories()
                .map{ it.map { RecordCategory(it.id, it.name, it.order, it.logo ?:"")}}
    }

    override fun newCategory(model: NewRecordCategoryRequest): Observable<Boolean> {
        return recordsRemoteDataSource.createRecordCategory(io.forus.me.android.data.entity.records.request.CreateCategory(model.order, model.name))
                .map { true }
    }

    override fun getCategory(id: Long): Observable<RecordCategory> {
        return recordsMockDataSource.retrieveRecordCategory(id)
                .map{ RecordCategory(it.id, it.name, it.order, it.logo ?:"")}
    }

    override fun getRecords(recordCategoryId: Long): Observable<List<Record>> {
        return getCategory(recordCategoryId).flatMap {
            recordsMockDataSource.getRecords(it.name)
                    .map{ it.map { Record(it.id, it.value, it.order, it.key, it.recordCategoryId, it.valid, it.validations)}}
                    .delay(300, TimeUnit.MILLISECONDS)
        }
    }

    override fun newRecord(model: NewRecordRequest): Observable<NewRecordRequest> {
        val createRecord = io.forus.me.android.data.entity.records.request.CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsRemoteDataSource.createRecord(createRecord)
                .flatMap {
                    Observable.just(model)
                }
    }

    override fun getRecord(id: Long): Observable<Record> {
        return recordsMockDataSource.retrieveRecord(id)
                .map{ Record(it.id, it.value, it.order, it.key, it.recordCategoryId, it.valid, it.validations)}
                .delay(300, TimeUnit.MILLISECONDS)
    }
}