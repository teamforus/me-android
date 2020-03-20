package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface ConfirmRegistrationView : LRView<ConfirmRegistrationModel> {



    fun exchangeToken(): io.reactivex.Observable<String>
}