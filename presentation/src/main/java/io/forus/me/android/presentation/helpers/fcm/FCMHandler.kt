package io.forus.me.android.presentation.helpers.fcm

import android.app.Activity

import android.util.Log

import com.google.firebase.messaging.FirebaseMessaging
import io.forus.me.android.data.repository.settings.SettingsDataSource
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FCMHandler(private val accountRepository: AccountRepository, private val settings: SettingsDataSource) {

    fun checkFCMToken(activity: Activity): Observable<Unit> {
        return Observable.fromPublisher<Unit> { publisher ->

            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                if (!token.isNullOrEmpty()) {
                    Log.d(TAG, "Retrieve new token successful, token: $token")
                    if (settings.getFCMToken() != token) {
                        registerFCMToken(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { publisher.onComplete() }
                            .onErrorReturn { publisher.onError(it) }
                            .subscribe()
                    }
                } else {
                    Log.w(TAG, "Token is null or empty")
                }
            }

        }
    }

    fun registerFCMToken(token: String): Observable<Unit> {
        return accountRepository.registerFCMToken(token)
                .map {
                    Log.d("FCM_TOKEN_REGISTERED", token)
                    settings.setFCMToken(token)
                    Unit
                }
                .onErrorReturn {
                    Log.e("FCM_TOKEN_REGISTER_ERR", it.message, it)
                }
    }

    fun clearFCMToken()= Observable.fromCallable {
            FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
                Log.d("FCM_TOKEN_CLEAR", "OK")
                Unit
            }
        }.doOnError {
            Log.e("FCM_TOKEN_CLEAR_THROWS", it.message?:"")
        }




    companion object {
        const val TAG = "FCMHandler"
    }
}