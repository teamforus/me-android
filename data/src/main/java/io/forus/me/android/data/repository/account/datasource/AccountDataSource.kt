package io.forus.me.android.data.repository.account.datasource

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.reactivex.Observable


interface AccountDataSource {


    fun createUser(signUp: SignUp): Observable<SignUpResult>


    fun restoreByQrToken() : Observable<IdentityTokenResult>


    fun restoreByEmail(email: String) : Observable<AccessToken>


    fun restoreByPinCode() : Observable<IdentityPinResult>


    fun saveIdentity(token: String, pin: String): Boolean


    fun unlockIdentity(pin: String): Boolean


    fun isLogin() : Boolean


    fun logout()

}
