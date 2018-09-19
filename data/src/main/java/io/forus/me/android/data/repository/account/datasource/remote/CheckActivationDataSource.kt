package io.forus.me.android.data.repository.account.datasource.remote

import com.gigawatt.android.data.net.sign.SignService
import io.forus.me.android.data.entity.sign.response.CheckTokenResult
import io.reactivex.Observable

class CheckActivationDataSource(val service: SignService){

    fun checkActivation(token: String): Observable<Boolean> = service.checkToken(token).map { it.state == CheckTokenResult.State.active }
}