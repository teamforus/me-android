package io.forus.me.android.presentation.helpers.fcm

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import io.forus.me.android.data.repository.settings.SettingsDataSource
import io.forus.me.android.domain.repository.account.AccountRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FCMHandler(private val accountRepository: AccountRepository, private val settings: SettingsDataSource) {

    fun checkFCMToken(activity: Activity): Observable<Unit> {
        return Observable.fromPublisher<Unit> { publisher ->
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(activity) { instanceIdResult ->
                val token = instanceIdResult.token
                Log.d("FCM_TOKEN", token)
                if (settings.getFCMToken() != token) {
                    registerFCMToken(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { publisher.onComplete() }
                            .onErrorReturn { publisher.onError(it) }
                            .subscribe()
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

    fun clearFCMToken(): Observable<Unit> {
        return Observable.fromCallable {
            FirebaseInstanceId.getInstance().deleteInstanceId()
            Log.d("FCM_TOKEN_CLEAR", "OK")
            Unit
        }.doOnError {
            Log.e("FCM_TOKEN_CLEAR_THROWS", it.message?:"")
        }
    }
}