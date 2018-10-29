package io.forus.me.android.data.repository.account

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.forus.me.android.data.entity.sign.request.SignRecords
import io.forus.me.android.data.repository.account.datasource.AccountDataSource
import io.forus.me.android.data.repository.account.datasource.remote.CheckActivationDataSource
import io.forus.me.android.data.repository.records.RecordsRepository
import io.forus.me.android.data.repository.settings.SettingsDataSource
import io.forus.me.android.domain.models.account.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

class AccountRepository(private val settingsDataSource: SettingsDataSource,
                        private val accountLocalDataSource: AccountDataSource,
                        private val accountRemoteDataSource: AccountDataSource,
                        private val checkActivationDataSource: CheckActivationDataSource,
                        private val recordsRepository: RecordsRepository) : io.forus.me.android.domain.repository.account.AccountRepository {

    override fun newUser(model: NewAccountRequest): Observable<String> {
        val signUp = SignUp()
        signUp.pinCode = "1111"
        signUp.records = SignRecords(model.email, model.firstname, model.lastname, model.bsn, model.phoneNumber)

       return accountRemoteDataSource.createUser(signUp)
                .flatMap {
                    Observable.just(it.accessToken)
                }.delay(50, TimeUnit.MILLISECONDS)

    }

    override fun restoreByEmail(email: String): Observable<Boolean> {
        return accountRemoteDataSource.restoreByEmail(email)
    }

    override fun restoreExchangeToken(token: String): Observable<RequestDelegatesEmailModel> {
        return accountRemoteDataSource.restoreExchangeToken(token)
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
        return Single.fromCallable {
            accountLocalDataSource.saveIdentity(identity.accessToken, identity.pin)
            settingsDataSource.clear()
            if(!settingsDataSource.setPin(identity.pin))
                throw IllegalStateException("PIN set error")
            true

        }.toObservable().delay(50, TimeUnit.MILLISECONDS)
    }

    override fun registerFCMToken(token: String): Observable<Boolean> {
        return accountRemoteDataSource.registerPush(token)
    }

    override fun unlockIdentity(pin: String): Observable<Boolean> {
        return Single.just(accountLocalDataSource.unlockIdentity(pin)).toObservable()
    }

    override fun checkPin(pin: String): Observable<Boolean> {
        return try {
                Single.just(settingsDataSource.getPin() == pin)
            }
            catch(e: Exception){
                Single.error<Boolean>(e)
            }
        .toObservable()
    }

    override fun setFingerprintEnabled(isFingerprintEnabled: Boolean): Observable<Boolean> {
        return Single.fromCallable { settingsDataSource.setFingerprintEnabled(isFingerprintEnabled); true }.toObservable()
    }


    override fun changePin(oldPin: String, newPin: String): Observable<Boolean> {
        return Single.fromCallable{
            if(accountLocalDataSource.changePin(oldPin, newPin)) {
                if(newPin == "") settingsDataSource.setFingerprintEnabled(false)
                settingsDataSource.setPin(newPin)
            } else false
        }.toObservable()
    }

    override fun exitIdentity(): Observable<Boolean> {
        return Single.fromCallable {
            accountLocalDataSource.logout()
            settingsDataSource.clear()
            true
        }.toObservable()
    }

    override fun getAccount(): Observable<Account> {
        return Observable.zip(
                recordsRepository.getRecords(),
                accountRemoteDataSource.getIdentity(),
                BiFunction{ records, identity ->

                    val account = Account()
                    val email = com.annimon.stream.Stream.of(records).filter { x->x.recordType.key == "primary_email" }.findFirst()
                    val givanName = com.annimon.stream.Stream.of(records).filter { x->x.recordType.key == "given_name" }.findFirst()
                    val familyName = com.annimon.stream.Stream.of(records).filter { x->x.recordType.key == "family_name" }.findFirst()
                    account.name = (if (givanName.isPresent) "${givanName.get().value} " else "") + (if (familyName.isPresent) "${familyName.get().value}" else "")
                    account.email = if (email.isPresent) email.get().value else ""
                    account.address = identity.address

                    account
                }
        )
    }

    override fun getSecurityOptions(): Observable<SecurityOptions> {
        return Single.zip(
                Single.just(settingsDataSource.isPinEnabled()),
                Single.just(settingsDataSource.isFingerprintEnabled()),
                BiFunction { pinEnabled: Boolean, fingerprintEnabled: Boolean ->
                    SecurityOptions(pinEnabled, fingerprintEnabled)
                }
        ).toObservable()
    }

    override fun checkCurrentToken(): Observable<Boolean>{
        return checkActivationDataSource.checkActivation(accountLocalDataSource.getCurrentToken())
    }

    override fun unlockByFingerprint(): Observable<Boolean> {
        return Single.just(accountLocalDataSource.unlockIdentity(settingsDataSource.getPin())).toObservable()
    }
}
