package io.forus.me.android.data.repository.records.datasource

import io.forus.me.android.data.entity.database.RecordCategory
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.reactivex.Observable


interface RecordsDataSource {


    fun getRecords(): Observable<List<RecordCategory>>
//
//    fun createAccount(): Observable<org.web3j.crypto.Credentials>
}
