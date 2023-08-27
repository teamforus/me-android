package io.forus.me.android.data.repository.account.datasource.remote

import com.gigawatt.android.data.net.sign.models.request.EmailValidateRequest
import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.account.Account
import io.forus.me.android.data.entity.sign.request.*
import io.forus.me.android.data.entity.sign.response.*
import io.forus.me.android.data.net.sign.SignService
import io.forus.me.android.data.repository.account.datasource.AccountDataSource
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.forus.me.android.domain.models.account.ValidateEmail
import io.reactivex.Observable


class AccountRemoteDataSource(f: () -> SignService) : AccountDataSource, RemoteDataSource<SignService>(f) {



    override fun createUser(signUp: SignUp): Observable<Boolean> {
        return service.signup(signUp).map {
            val result = it.string();
            result == "{}" || result.contains("\"access_token\"")
        }
    }

    override fun saveIdentity(token: String, pin: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unlockIdentity(pin: String): Boolean {
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
        return service.authorizeToken(AuthorizeToken(token)).map {
            val result = it.string();
            result == "{}"
        }
    }

    override fun restoreByEmail(email: String): Observable<Boolean> {
        val signUp = RestoreByEmail(email)
        return service.restoreByEmail(signUp).map {
            val result = it.string();
            result == "{}" || result.contains("\"access_token\"") || result.contains("\"success\"")
        }
    }

    override fun validateEmail(email: String): Observable<ValidateEmail> {
        val requestBody = EmailValidateRequest()
        requestBody.email = email

       /* val observable: Observable<Boolean> = ObservableCreate<Boolean>(object : ObservableOnSubscribe<Boolean?> {
            override fun subscribe(emitter: ObservableEmitter<Boolean?>) {
                true
            }

        })*/
        return service.validateEmail(requestBody)
                .map {
                    io.forus.me.android.domain.models.account.ValidateEmail(it.email.used, it.email.valid)
                }
    }

    override fun restoreExchangeToken(token: String): Observable<AccessToken> {
        return service.restoreExchangeToken(token);
    }

    override fun registerExchangeToken(token: String): Observable<AccessToken> {
        return service.registerExchangeToken(token)
    }

    override fun registerPush(token: String): Observable<Boolean> {
        return service.registerPush(RegisterPush(token)).map { true }
    }

    override fun getIdentity(): Observable<Account> {
        return service.getIdentity()
    }

    override fun getShortToken(): Observable<ShortTokenResult> {
        return service.getShortToken()
    }

    override fun getFirestoreToken(userUid: String): Observable<FirestoreToken> {
        return service.getFirestoreToken(UserUid(userUid))
    }
}