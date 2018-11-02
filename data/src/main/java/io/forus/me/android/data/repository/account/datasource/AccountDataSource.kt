package io.forus.me.android.data.repository.account.datasource

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.account.Account
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.reactivex.Observable


interface AccountDataSource {


    fun createUser(signUp: SignUp): Observable<SignUpResult>


    fun restoreByQrToken() : Observable<IdentityTokenResult>


    fun restoreByEmail(email: String) : Observable<Boolean>


    fun restoreExchangeToken(token: String) : Observable<AccessToken>


    fun restoreByPinCode() : Observable<IdentityPinResult>


    fun authorizeCode(code: String): Observable<Boolean>


    fun authorizeToken(token: String): Observable<Boolean>


    fun registerPush(token: String): Observable<Boolean>


    fun getIdentity(): Observable<Account>


    fun saveIdentity(token: String, pin: String)


    fun unlockIdentity(pin: String): Boolean


    fun isLogin() : Boolean


    fun checkPin(pin: String): Boolean


    fun changePin(oldPin: String, newPin: String): Boolean


    fun logout()


    fun getCurrentToken(): String

}
