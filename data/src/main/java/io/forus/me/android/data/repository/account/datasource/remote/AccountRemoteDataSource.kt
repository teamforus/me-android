package io.forus.me.android.data.repository.account.datasource.remote

import com.gigawatt.android.data.net.sign.SignService
import com.gigawatt.android.data.net.sign.models.request.SignUp
import com.gigawatt.android.data.net.sign.models.request.SignUpByEmail
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

    override fun requestDelegatesQRAddress(): Observable<IdentityTokenResult> {
        return service.identityToken()
    }

    override fun getAuthCode(): Observable<IdentityPinResult> {
        return service.authCode()
    }

    override fun requestNewUserByEmail(email: String): Observable<AccessToken> {
        val signUp = SignUpByEmail(email)
        return service.requestNewUserByEmail(signUp)
    }
}