package io.forus.me.android.data.repository.account

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.request.SignRecords
import io.forus.me.android.data.repository.account.datasource.AccountDataSource
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest
import io.reactivex.Observable

class AccountRepository(private val accountLocalDataSource: AccountDataSource,private val accountRemoteDataSource: AccountDataSource) : io.forus.me.android.domain.repository.account.AccountRepository {

    override fun requestDelegatesQRAddress(): Observable<RequestDelegatesQrModel> {
        return accountRemoteDataSource.requestDelegatesQRAddress()
                .map {
                    accountLocalDataSource.saveToken(it.accessToken)

                    RequestDelegatesQrModel(it.authToken)
                }
     //   return Observable.just(RequestDelegatesQrModel("ADDRESS"))//accountLocalDataSource.requestDelegatesQRAddress().map { x: String -> RequestDelegatesQrModel(x) }
    }

//    override fun createAccount(): Observable<String> {
//        return accountLocalDataSource.createAccount().map {
//            it.address
//        }
//    }

    override fun newUser(model: NewAccountRequest): Observable<NewAccountRequest> {
        val signUp = SignUp()
        signUp.pinCode = "6666"
        signUp.records = SignRecords(model.email)

       return accountRemoteDataSource.createUser(signUp)
                .flatMap {
                    accountLocalDataSource.saveToken(it.accessToken)
                    Observable.just(model)
                }

    }

    override fun loginByEmail(email: String): Observable<RestoreAccountByEmailRequest> {
        return accountRemoteDataSource.requestNewUserByEmail(email)
                .map {
                    accountLocalDataSource.saveToken(it.accessToken)
                    RestoreAccountByEmailRequest(it.accessToken)
                }
    }

    override fun getLoginPin(): Observable<RequestDelegatesPinModel> {
        return accountRemoteDataSource.getAuthCode()
                .map {
                    RequestDelegatesPinModel(it.authCode)
                }
    }
}
