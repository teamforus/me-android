package io.forus.me.android.data.repository.account

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.request.SignRecords
import io.forus.me.android.data.repository.account.datasource.AccountDataSource
import io.forus.me.android.domain.models.account.*
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class AccountRepository(private val accountLocalDataSource: AccountDataSource, private val accountRemoteDataSource: AccountDataSource) : io.forus.me.android.domain.repository.account.AccountRepository {

    override fun newUser(model: NewAccountRequest): Observable<String> {
        val signUp = SignUp()
        signUp.pinCode = "1111"
        signUp.records = SignRecords(model.email, model.firstname, model.lastname, model.bsn, model.phoneNumber)

       return accountRemoteDataSource.createUser(signUp)
                .flatMap {
                    Observable.just(it.accessToken)
                }.delay(100, TimeUnit.MILLISECONDS)

    }

    override fun restoreByEmail(email: String): Observable<RequestDelegatesEmailModel> {
        return accountRemoteDataSource.restoreByEmail(email)
                .map {
                    RequestDelegatesEmailModel(it.accessToken)
                }
    }

    override fun restoreByQrToken(): Observable<RequestDelegatesQrModel> {
        return accountRemoteDataSource.restoreByQrToken()
                .map {
                    RequestDelegatesQrModel(it.accessToken, it.authToken)
                }
    }

    override fun restoreByPinCode(): Observable<RequestDelegatesPinModel> {
        return accountRemoteDataSource.restoreByPinCode()
                .map {
                    RequestDelegatesPinModel(it.accessToken, it.authCode)
                }
    }

    override fun authorizeCode(code: String): Observable<Boolean> {
        return  accountRemoteDataSource.authorizeCode(code)
    }

    override fun authorizeToken(token: String): Observable<Boolean> {
        return accountRemoteDataSource.authorizeToken(token)
    }

    override fun createIdentity(identity: Identity): Observable<Boolean> {
        return Single.just(accountLocalDataSource.saveIdentity(identity.accessToken, identity.pin)).toObservable()
                .delay(100, TimeUnit.MILLISECONDS)
    }

    override fun unlockIdentity(pin: String): Observable<Boolean> {
        return Single.just(accountLocalDataSource.unlockIdentity(pin)).toObservable()
                //.delay(100, TimeUnit.MILLISECONDS)
    }

    override fun exitIdentity(): Observable<Boolean> {
        return Single.fromCallable {accountLocalDataSource.logout(); true }.toObservable()
                .delay(100, TimeUnit.MILLISECONDS)
    }

    override fun getAccount(): Observable<Account> {
        val account = Account();
        account.name = "Adolph Nalevia Hollenbeck-Moore"
        account.email = "mail@mail.com"

        return Single.just(account).toObservable()
    }
}
