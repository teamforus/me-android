package io.forus.me.android.domain.repository.account

import io.forus.me.android.domain.models.account.*
import io.reactivex.Observable

interface AccountRepository {

    fun requestDelegatesQRAddress(): Observable<RequestDelegatesQrModel>


    fun newUser(model: NewAccountRequest) : Observable<String>


    fun loginByEmail(email: String) : Observable<RestoreAccountByEmailRequest>


    fun getLoginPin() : Observable<RequestDelegatesPinModel>


    fun createIdentity(identity: Identity): Observable<Boolean>


    fun unlockIdentity(pin: String): Observable<Boolean>


    fun exitIdentity(): Observable<Boolean>

}
