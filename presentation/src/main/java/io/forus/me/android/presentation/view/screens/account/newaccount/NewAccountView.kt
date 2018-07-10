package io.forus.me.android.presentation.view.screens.account.newaccount;

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.presentation.view.screens.account.newaccount.NewAccountModel

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface NewAccountView : LRView<NewAccountModel> {

    fun register(): io.reactivex.Observable<NewAccountRequest>

}