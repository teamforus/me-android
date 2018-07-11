package io.forus.me.android.data.repository.records.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.repository.records.datasource.RecordsDataSource
import io.reactivex.Observable


public class RecordsRemoteDataSource(private val recordsService: RecordsService): RecordsDataSource {


    override fun getRecords(): Observable<List<RecordCategory>> = recordsService.getCategories().map {
        it.map { RecordCategory(it.id, it.name, it.order) }
    }
}