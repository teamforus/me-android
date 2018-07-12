package io.forus.me.android.data.repository.records.datasource

import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.entity.records.response.CreateRecordResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.reactivex.Observable


interface RecordsDataSource {


    fun getRecords(): Observable<List<RecordCategory>>


    fun createRecord(model: NewRecordRequest): Observable<CreateRecordResult>


//
//    fun createAccount(): Observable<org.web3j.crypto.Credentials>
}
