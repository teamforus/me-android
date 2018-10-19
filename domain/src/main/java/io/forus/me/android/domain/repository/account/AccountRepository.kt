package io.forus.me.android.domain.repository.account

import io.forus.me.android.domain.models.account.*
import io.reactivex.Observable

interface AccountRepository {

    fun newUser(model: NewAccountRequest) : Observable<String>


    fun restoreByEmail(email: String) : Observable<Boolean>


    fun restoreExchangeToken(token: String) : Observable<RequestDelegatesEmailModel>


    fun restoreByQrToken(): Observable<RequestDelegatesQrModel>


    fun restoreByPinCode() : Observable<RequestDelegatesPinModel>


    fun authorizeCode(code: String): Observable<Boolean>


    fun authorizeToken(token: String): Observable<Boolean>


    fun createIdentity(identity: Identity): Observable<Boolean>


    fun unlockIdentity(pin: String): Observable<Boolean>


    fun checkPin(pin: String): Observable<Boolean>


    fun setFingerprintEnabled(isFingerprintEnabled: Boolean): Observable<Boolean>


    fun changePin(oldPin: String, newPin: String): Observable<Boolean>


    fun exitIdentity(): Observable<Boolean>


    fun getAccount(): Observable<Account>


    fun getSecurityOptions(): Observable<SecurityOptions>


    fun checkCurrentToken(): Observable<Boolean>


    fun unlockByFingerprint(): Observable<Boolean>
}
