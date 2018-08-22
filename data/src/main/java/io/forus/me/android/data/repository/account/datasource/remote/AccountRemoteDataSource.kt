package io.forus.me.android.data.repository.account.datasource.remote

import com.gigawatt.android.data.net.sign.SignService
import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.request.RestoreByEmail
import io.forus.me.android.data.entity.sign.response.AccessToken
import io.forus.me.android.data.entity.sign.response.IdentityPinResult
import io.forus.me.android.data.entity.sign.response.IdentityTokenResult
import io.forus.me.android.data.entity.sign.response.SignUpResult
import io.forus.me.android.data.repository.account.datasource.AccountDataSource
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.reactivex.Observable

public class AccountRemoteDataSource(f: () -> SignService): AccountDataSource, RemoteDataSource<SignService>(f) {

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

    override fun restoreByQrToken(): Observable<IdentityTokenResult> {
        return service.restoreByQrToken()
    }

    override fun restoreByPinCode(): Observable<IdentityPinResult> {
        return service.restoreByPinCode()
    }

    override fun restoreByEmail(email: String): Observable<AccessToken> {
        val signUp = RestoreByEmail(email)
        return service.restoreByEmail(signUp)
    }
}