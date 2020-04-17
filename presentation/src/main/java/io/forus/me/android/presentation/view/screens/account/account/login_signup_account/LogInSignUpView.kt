package io.forus.me.android.presentation.view.screens.account.account.login_signup_account

import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.presentation.view.base.lr.LRView




interface LogInSignUpView : LRView<LogInSignUpModel> {

    fun register(): io.reactivex.Observable<String>

    fun exchangeToken(): io.reactivex.Observable<String>

}