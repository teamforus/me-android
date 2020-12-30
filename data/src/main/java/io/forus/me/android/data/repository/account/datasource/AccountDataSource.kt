package io.forus.me.android.data.repository.account.datasource

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.account.Account
import io.forus.me.android.data.entity.sign.response.*
import io.reactivex.Observable


interface AccountDataSource {


    fun createUser(signUp: SignUp): Observable<Boolean>


    fun restoreByQrToken() : Observable<IdentityTokenResult>


    fun restoreByEmail(email: String) : Observable<Boolean>

    fun validateEmail(email: String) : Observable<io.forus.me.android.domain.models.account.ValidateEmail>


    fun restoreExchangeToken(token: String) : Observable<AccessToken>

    fun registerExchangeToken(token: String) : Observable<AccessToken>


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


    fun getShortToken() : Observable<ShortTokenResult>

}
