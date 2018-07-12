package io.forus.me.android.data.repository.records

import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.RecordCategory
import io.reactivex.Observable
import io.reactivex.Single

class RecordsRepository(private val recordsRemoteDataSource: RecordsDataSource) : io.forus.me.android.domain.repository.records.RecordsRepository {

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

    override fun getCategories(): Observable<List<RecordCategory>> {
        return recordsRemoteDataSource.getRecords().map {
            it.map { RecordCategory(it.id, it.name, it.order) }
        }
    }

    override fun newRecord(model: NewRecordRequest): Observable<NewRecordRequest> {
        return recordsRemoteDataSource.createRecord(model)
                .flatMap {

                    Observable.just(model)
                }
    }
}