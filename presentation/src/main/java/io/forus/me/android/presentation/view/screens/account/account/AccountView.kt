package io.forus.me.android.presentation.view.screens.account.account;

import io.forus.me.android.presentation.view.base.lr.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface AccountView : LRView<AccountModel> {



    fun logout(): io.reactivex.Observable<Boolean>


}