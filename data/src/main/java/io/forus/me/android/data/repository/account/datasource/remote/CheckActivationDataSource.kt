package io.forus.me.android.data.repository.account.datasource.remote

import com.gigawatt.android.data.net.sign.RecordsService
import io.reactivex.Observable

class CheckActivationDataSource(val service: RecordsService){

    fun checkActivation(): Observable<Boolean> = service.listAllCategories().map { true }
}