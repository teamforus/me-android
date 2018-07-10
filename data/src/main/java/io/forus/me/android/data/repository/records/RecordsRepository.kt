package io.forus.me.android.data.repository.records

import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.RecordType
import io.reactivex.Observable
import io.reactivex.Single

class RecordsRepository : io.forus.me.android.domain.repository.records.RecordsRepository {

    override fun getRecords(): Observable<List<Record>> {
        val records : MutableList<Record> = mutableListOf()

        val personal = RecordType("1", "Personal", "https://www.freelogodesign.org/Content/img/logo-ex-7.png")
        val medical = RecordType("2", "Medical", "https://www.freelogodesign.org/Content/img/logo-ex-7.png")
        val professional = RecordType("2", "Professional", "https://www.freelogodesign.org/Content/img/logo-ex-7.png")

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
}