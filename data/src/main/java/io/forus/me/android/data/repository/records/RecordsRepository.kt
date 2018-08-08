package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.data.repository.records.datasource.mock.RecordsMockDataSource
import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import java.util.concurrent.TimeUnit

class RecordsRepository(private val recordsMockDataSource: RecordsMockDataSource, private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

    override fun getRecordTypes(): Observable<List<RecordType>> {
        return recordsMockDataSource.getRecordTypes()
                .map { it.map { RecordType(it.key, it.type, it.name) }}
    }

    override fun getCategories(): Observable<List<RecordCategory>> {
        return recordsMockDataSource.getRecordCategories().flatMap {
            val list: ArrayList<RecordCategory> = ArrayList()
            it.forEach{
                recordsMockDataSource.getRecords(it.name).blockingSubscribe{records ->
                    list.add(RecordCategory(it.id, it.name, it.order, it.logo ?:"", records.size.toLong()))
                }
            }
            Observable.just(list).delay(300, TimeUnit.MILLISECONDS)
        }

//        return recordsMockDataSource.getRecordCategories()
//                .map{ it.map { RecordCategory(it.id, it.name, it.order, it.logo ?:"")} }
//                .delay(500, TimeUnit.MILLISECONDS)
    }

    override fun newCategory(newRecordCategoryRequest: NewRecordCategoryRequest): Observable<Boolean> {
        return recordsRemoteDataSource.createRecordCategory(io.forus.me.android.data.entity.records.request.CreateCategory(newRecordCategoryRequest.order, newRecordCategoryRequest.name))
                .map { true }
    }

    override fun getCategory(categoryId: Long): Observable<RecordCategory> {
        return recordsMockDataSource.retrieveRecordCategory(categoryId)
                .map{ RecordCategory(it.id, it.name, it.order, it.logo ?:"")}
    }

    override fun getRecords(recordCategoryId: Long): Observable<List<Record>> {
        return Single.zip(
                Single.fromObservable(getCategory(recordCategoryId)),
                Single.fromObservable(getRecordTypes()),
                BiFunction { category : RecordCategory, types: List<RecordType> ->
                    recordsMockDataSource.getRecords(category.name)
                        .map{ list ->
                            list.map {
                                val type = types.find { type -> type.key.equals(it.key) }
                                Record(it.id, it.value, it.order, type!!, category, it.valid, it.validations)
                            }
                        }
                }
        ).flatMapObservable {
            it
        }.delay(300, TimeUnit.MILLISECONDS)
    }

    override fun newRecord(model: NewRecordRequest): Observable<NewRecordRequest> {
        val createRecord = io.forus.me.android.data.entity.records.request.CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsMockDataSource.createRecord(createRecord)
                .flatMap {
                    Observable.just(model)
                }
    }

    override fun getRecord(recordId: Long): Observable<Record> {
        return Single.zip(
                Single.fromObservable(recordsMockDataSource.retrieveRecord(recordId)),
                Single.fromObservable(getRecordTypes()),
                BiFunction { record : io.forus.me.android.data.entity.records.response.Record, types: List<RecordType> ->
                    getCategory(record.recordCategoryId)
                            .map {
                                val type = types.find { type -> type.key.equals(record.key) }
                                Record(record.id, record.value, record.order, type!!, it, record.valid, record.validations)
                            }
                }
        ).flatMapObservable {
            it
        }.delay(300, TimeUnit.MILLISECONDS)
    }

    override fun getRecordUuid(recordId: Long): Observable<String> {
        return recordsMockDataSource.createValidationToken(recordId)
                .map { it.uuid }
    }
}