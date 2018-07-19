package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.*
import io.reactivex.Observable
import io.reactivex.Single

class RecordsRepository(private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

    override fun getRecordTypes(): Observable<List<RecordType>> {
        return recordsRemoteDataSource.getRecordTypes()
                .map { it.map { RecordType(it.key, it.type, it.name) }}
    }

    override fun getCategories(): Observable<List<RecordCategory>> {
        val categories : MutableList<RecordCategory> = mutableListOf()
        for (i in 0..6) {
            categories.add(RecordCategory(i.toLong(), "Persoonlijk$i", i.toLong(), "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/thinking-face_1f914.png"))
            categories.add(RecordCategory(i.toLong()*10, "Medisch$i", i.toLong()*10, "https://emojipedia-us.s3.amazonaws.com/thumbs/120/apple/129/syringe_1f489.png"))
            categories.add(RecordCategory(i.toLong()*100, "Professional$i", i.toLong()*100))
        }
        return Single.just(categories.toList()).toObservable()


//        return recordsRemoteDataSource.getRecordCategories()
//                .flatMap {
//                    if (it.isNotEmpty()) {
//                        Single.just(it.map {
//                            RecordCategory(it.id, it.name ?: "noname", it.order)
//                        }).toObservable()
//                    } else {
//                        val general = io.forus.me.android.domain.models.records.NewRecordCategoryRequest("General", 0)
//                        val medical = io.forus.me.android.domain.models.records.NewRecordCategoryRequest("Medical", 1)
//                        val personal = io.forus.me.android.domain.models.records.NewRecordCategoryRequest("Personal", 2)
//                        Observable.concat(newCategory(general), newCategory(medical), newCategory(personal)).flatMap {
//                            getCategories()
//                        }
//                    }
//                }
    }

    override fun newCategory(model: NewRecordCategoryRequest): Observable<Boolean> {
        return recordsRemoteDataSource.createRecordCategory(io.forus.me.android.data.entity.records.request.CreateCategory(model.order, model.name))
                .map { true }
    }

    override fun getCategory(id: Long): Observable<RecordCategory> {
        return recordsRemoteDataSource.retrieveRecordCategory(id)
                .map{ RecordCategory(it.id, it.name ?: "noname", it.order, "")}
    }

    override fun getRecords(): Observable<List<Record>> {
        val records : MutableList<Record> = mutableListOf()

        val personal = RecordCategory(1, "Personal", 1)
        val medical = RecordCategory(2, "Medical", 2)
        val professional = RecordCategory(3, "Professional", 3)

        for (i in 0..6) {
            records.add(Record(i.toString(), "Title", "Value", personal))
        }


        for (i in 0..6) {
            records.add(Record((i*10).toString(), "Title", "Value", medical))
        }

        for (i in 0..6) {
            records.add(Record((i*100).toString(), "Title", "Value", professional))
        }

        return Single.just(records.toList()).toObservable()
    }

    override fun newRecord(model: NewRecordRequest): Observable<NewRecordRequest> {
        val createRecord = io.forus.me.android.data.entity.records.request.CreateRecord(model.recordType?.key, model.category?.id, model.value, model.order)
        return recordsRemoteDataSource.createRecord(createRecord)
                .flatMap {
                    Observable.just(model)
                }
    }

    override fun getRecord(id: Long): Observable<Record> {
        return recordsRemoteDataSource.retrieveRecord(id)
                .map{ Record(if(it.id != null) it.id.toString() else "???", it.key ?: "key", it.value ?: "value", RecordCategory(it.recordCategoryId ?: -1, "", 0, ""))}
    }
}