package io.forus.me.android.presentation.view.screens.account.newaccount;

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.forus.me.android.domain.models.account.NewAccountRequest

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface AccountView : LRView<AccountModel> {



    fun logout(): io.reactivex.Observable<Boolean>


}