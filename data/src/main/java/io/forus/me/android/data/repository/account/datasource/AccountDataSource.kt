package io.forus.me.android.data.repository.account.datasource

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.reactivex.Observable


interface AccountDataSource {


    fun createUser(signUp: SignUp): Observable<SignUpResult>


    fun requestDelegatesQRAddress() : Observable<IdentityTokenResult>


    fun requestNewUserByEmail(email: String) : Observable<AccessToken>


    fun getAuthCode() : Observable<IdentityPinResult>

    fun saveToken(token: String)

    fun isLogin() : Boolean


    fun logout()

//
//    fun createAccount(): Observable<org.web3j.crypto.Credentials>
}
