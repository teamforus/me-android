package io.forus.me.android.presentation.view.screens.account.account;

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface AccountView : LRView<AccountModel> {



    fun logout(): io.reactivex.Observable<Boolean>


}