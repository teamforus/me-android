package io.forus.me.android.data.repository.account.datasource

import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.reactivex.Observable


interface AccountDataSource {


    fun createUser(model: NewAccountRequest): Observable<SignUpResult>


    fun requestDelegatesQRAddress() : Observable<IdentityTokenResult>


    fun requestNewUserByEmail(email: String) : Observable<AccessToken>


    fun getAuthCode() : Observable<IdentityPinResult>

    fun saveToken(token: String)

    fun isLogin() : Boolean


    fun logout()

//
//    fun createAccount(): Observable<org.web3j.crypto.Credentials>
}
