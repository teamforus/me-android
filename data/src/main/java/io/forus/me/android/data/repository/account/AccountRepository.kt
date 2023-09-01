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
import io.reactivex.functions.Function4
import java.util.concurrent.TimeUnit

class AccountRepository(private val settingsDataSource: SettingsDataSource,
                        private val accountLocalDataSource: AccountDataSource,
                        private val accountRemoteDataSource: AccountDataSource,
                        private val checkActivationDataSource: CheckActivationDataSource,
                        private val recordsRepository: RecordsRepository) : io.forus.me.android.domain.repository.account.AccountRepository {

    override fun registerExchangeToken(token: String): Observable<RequestDelegatesEmailModel> {
        return accountRemoteDataSource.registerExchangeToken(token)
                .map {
                    RequestDelegatesEmailModel(it.accessToken)
                }
    }


    override fun newUser(model: NewAccountRequest): Observable<Boolean> {
        val signUp = SignUp()
        signUp.email = model.email
        signUp.pinCode = "1111"

        return accountRemoteDataSource.createUser(signUp)
                .flatMap {
                    Observable.just(it)
                }.delay(50, TimeUnit.MILLISECONDS)

    }

    override fun restoreByEmail(email: String): Observable<Boolean> {
        return accountRemoteDataSource.restoreByEmail(email)
    }

    override fun validateEmail(email: String): Observable<ValidateEmail> {

        return accountRemoteDataSource.validateEmail(email).map {
            it
        }
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
        return accountRemoteDataSource.authorizeCode(code)
    }

    override fun authorizeToken(token: String): Observable<Boolean> {
        return accountRemoteDataSource.authorizeToken(token)
    }

    override fun createIdentity(identity: Identity): Observable<Boolean> {
        return Single.fromCallable {
            accountLocalDataSource.saveIdentity(identity.accessToken, identity.pin)
            settingsDataSource.clear()
            if (!settingsDataSource.setPin(identity.pin))
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
        } catch (e: Exception) {
            Single.error<Boolean>(e)
        }
                .toObservable()
    }

    override fun setFingerprintEnabled(isFingerprintEnabled: Boolean): Observable<Boolean> {
        return Single.fromCallable { settingsDataSource.setFingerprintEnabled(isFingerprintEnabled); true }.toObservable()
    }

    override fun setStartFromScannerEnabled(isEnabled: Boolean): Observable<Boolean> {
        return Single.fromCallable { settingsDataSource.setStartFromScannerEnabled(isEnabled); true }.toObservable()
    }

    override fun setSendCrashReportsEnabled(isEnabled: Boolean): Observable<Boolean> {
        return Single.fromCallable { settingsDataSource.setSendCrashReportsEnabled(isEnabled); true }.toObservable()
    }

    override fun getSendCrashReportsEnabled(): Observable<Boolean> {
        return Single.fromObservable<Boolean> { settingsDataSource.isSendCrashReportsEnabled() }.toObservable()
    }

    override fun changePin(oldPin: String, newPin: String): Observable<Boolean> {
        return Single.fromCallable {
            if (accountLocalDataSource.changePin(oldPin, newPin)) {
                if (newPin == "") settingsDataSource.setFingerprintEnabled(false)
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
            BiFunction { records, identity ->

                val account = Account()
                // val email = com.annimon.stream.Stream.of(records).filter { x->x.recordType.key == "primary_email" }.findFirst()
                val givanName = com.annimon.stream.Stream.of(records).filter { x -> x.recordType.key == "given_name" }.findFirst()
                val familyName = com.annimon.stream.Stream.of(records).filter { x -> x.recordType.key == "family_name" }.findFirst()
                account.name = (if (givanName.isPresent) "${givanName.get().value} " else "") + (if (familyName.isPresent) "${familyName.get().value}" else "")
                //account.email = if (email.isPresent) email.get().value else ""
                account.email = identity.email
                account.address = identity.address

                account
            }
        )
    }

    override fun getSecurityOptions(): Observable<SecurityOptions> {
        return Single.zip(
                Single.just(settingsDataSource.isPinEnabled()),
                Single.just(settingsDataSource.isFingerprintEnabled()),
                Single.just(settingsDataSource.isStartFromScannerEnabled()),
                Single.just(settingsDataSource.isSendCrashReportsEnabled()),
                Function4<Boolean, Boolean, Boolean, Boolean, SecurityOptions> { pinEnabled, fingerprintEnabled, startFromScannerEnabled, sendCrashReportsEnabled -> SecurityOptions(sendCrashReportsEnabled, pinEnabled, fingerprintEnabled, startFromScannerEnabled) }
        ).toObservable()
    }



    override fun checkCurrentToken(): Observable<Boolean> {

        return checkActivationDataSource.checkActivation(accountLocalDataSource.getCurrentToken())
    }

    override fun unlockByFingerprint(): Observable<Boolean> {
        return Single.just(accountLocalDataSource.unlockIdentity(settingsDataSource.getPin())).toObservable()
    }

    override fun getShortToken(): Observable<String> {
        return accountRemoteDataSource.getShortToken()
                .map {
                    it.exchangeToken
                }
    }

    override fun getFirestoreToken(userUid: String): Observable<String> {

        //Uncomment it

       /* return accountRemoteDataSource.getFirestoreToken(userUid)
            .map {
                it.token
            }*/

        //Mock

        return Observable.just(

            "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTY5MzYwODU4MywiaWF0IjoxNjkzNjA0OTgzLCJpc3MiOiJmb3J1cy1mZDhlOEBhcHBzcG90LmdzZXJ2aWNlYWNjb3VudC5jb20iLCJzdWIiOiJmb3J1cy1mZDhlOEBhcHBzcG90LmdzZXJ2aWNlYWNjb3VudC5jb20iLCJ1aWQiOiJ0ZXN0MkBhc2Rhc2QubmwifQ.Uf0KRgLlRThmM0JZ7C66tIvDu5HF-yWmSPJHr1vsinB-W1qiurAmhm7Sdu9f8qRUTn9Rggo5M99jR6619HGysc_x_dJ490JemKXI9Q0XvHe6ul_EHcnpxaGGvPPwtQkWTo2PFK1RPvE_ymRc_P_v21Ak1O2MscJ9Fdj45nSC-VA2UI55htFI_SI1-H8x2fGqvIhF2svxL2ZhadxJSv3-tzwWF-OuhcKrdozyYx3iqQziuQlIE6zG1qUX2fkOANqGKC_CtPzcSeKQiO3BksbpZ70xawDDRR7kx9fjrTrzCxiMfs_aXFd441Ts-5KAxoSjoG0JU42zeeZIL-8hcMh33g"

        )


    }


}
