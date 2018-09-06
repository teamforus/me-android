package io.forus.me.android.presentation.view.screens.account.newaccount;

import io.forus.me.android.presentation.view.base.lr.LRView
import io.forus.me.android.domain.models.account.NewAccountRequest

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface NewAccountView : LRView<NewAccountModel> {

    fun register(): io.reactivex.Observable<NewAccountRequest>

}