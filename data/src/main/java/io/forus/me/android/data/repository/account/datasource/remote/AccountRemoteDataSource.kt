package io.forus.me.android.data.repository.account.datasource.remote

import com.gigawatt.android.data.net.sign.SignService
import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.request.AuthorizeCode
import io.forus.me.android.data.entity.sign.request.AuthorizeToken
import io.forus.me.android.data.entity.sign.request.RestoreByEmail
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.forus.me.android.data.repository.account.datasource.AccountDataSource
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.reactivex.Observable

class AccountRemoteDataSource(f: () -> SignService): AccountDataSource, RemoteDataSource<SignService>(f) {

    override fun createUser(signUp: SignUp): Observable<SignUpResult> {
        return service.signup(signUp)
    }

    override fun saveIdentity(token: String, pin: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override  fun unlockIdentity(pin: String): Boolean{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLogin(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkPin(pin: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changePin(oldPin: String, newPin: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentToken(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun restoreByQrToken(): Observable<IdentityTokenResult> {
        return service.restoreByQrToken()
    }

    override fun restoreByPinCode(): Observable<IdentityPinResult> {
        return service.restoreByPinCode()
    }

    override fun authorizeCode(code: String): Observable<Boolean> {
        return service.authorizeCode(AuthorizeCode(code)).map { it.success }
    }

    override fun authorizeToken(token: String): Observable<Boolean> {
        return service.authorizeToken(AuthorizeToken(token)).map { it.success }
    }

    override fun restoreByEmail(email: String): Observable<Boolean> {
        val signUp = RestoreByEmail(email)
        return service.restoreByEmail(signUp).map { it.success }
    }

    override fun restoreExchangeToken(token: String): Observable<AccessToken> {
        return service.restoreExchangeToken(token);
    }
}