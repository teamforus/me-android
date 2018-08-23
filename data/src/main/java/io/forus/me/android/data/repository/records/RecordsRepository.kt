package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.data.repository.records.datasource.mock.RecordsMockDataSource
import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class RecordsRepository(private val recordsMockDataSource: RecordsMockDataSource, private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

    private val defaultCategoryNames: List<String> = listOf("Persoonlijk", "Medical", "Zakelijk", "Relaties", "Certificaten", "Anderen")

    override fun getRecordTypes(): Observable<List<RecordType>> {
        return recordsMockDataSource.getRecordTypes()
                .map { it.map { RecordType(it.key, it.type, it.name) }}
    }

    override fun getCategories(): Observable<List<RecordCategory>> {

            return recordsRemoteDataSource.getRecordCategories()
                .flatMap { categories ->
                    val result: MutableList<RecordCategory> = mutableListOf()
                    val newCategoryNames: MutableList<String> = mutableListOf()
                    defaultCategoryNames.forEach{ name ->
                        if(!categories.map{it.name}.contains(name)){
                            newCategoryNames.add(name)
                        }
                    }
                    if(newCategoryNames.isNotEmpty()){
                        newCategoryNames.forEach {
                            newCategory(NewRecordCategoryRequest(it, 0)).blockingSubscribe()
                        }
                        recordsRemoteDataSource.getRecordCategories().map { newCategories ->
                            newCategories.forEach{
                                getRecordsCount(it.id).blockingSubscribe{recordsCount ->
                                    result.add(RecordCategory(it.id, it.name, it.order, recordsCount))
                                }
                            }
                            Observable.just(result)
                        }
                    }
                    else{
                        categories.forEach{
                            getRecordsCount(it.id).blockingSubscribe{recordsCount ->
                                result.add(RecordCategory(it.id, it.name, it.order, recordsCount))
                            }
                        }
                    }
                    Observable.just(result)
                }
    }

    override fun newCategory(newRecordCategoryRequest: NewRecordCategoryRequest): Observable<Boolean> {
        return recordsRemoteDataSource.createRecordCategory(io.forus.me.android.data.entity.records.request.CreateCategory(newRecordCategoryRequest.order, newRecordCategoryRequest.name))
                .map { true }
    }

    override fun getCategory(categoryId: Long): Observable<RecordCategory> {
        return recordsMockDataSource.retrieveRecordCategory(categoryId)
                .map{ RecordCategory(it.id, it.name, it.order)}
    }

    override fun getRecordsCount(recordCategoryId: Long): Observable<Long> {
        return recordsRemoteDataSource.getRecords(recordCategoryId).map {it.size.toLong()}
    }

    override fun getRecords(recordCategoryId: Long): Observable<List<Record>> {
        return Single.zip(
                Single.fromObservable(getCategory(recordCategoryId)),
                Single.fromObservable(getRecordTypes()),
                BiFunction { category : RecordCategory, types: List<RecordType> ->
                    recordsRemoteDataSource.getRecords(recordCategoryId)
                        .map{ list ->
                            list.map {
                                val type = types.find { type -> type.key.equals(it.key) }
                                Record(it.id, it.value, it.order, type!!, category, it.valid, it.validations)
                            }
                        }
                }
        ).flatMapObservable {
            it
        }
    }

    override fun newRecord(model: NewRecordRequest): Observable<NewRecordRequest> {
        val createRecord = io.forus.me.android.data.entity.records.request.CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsMockDataSource.createRecord(createRecord)
                .flatMap {
                    Observable.just(model)
                }.delay(300, TimeUnit.MILLISECONDS)
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