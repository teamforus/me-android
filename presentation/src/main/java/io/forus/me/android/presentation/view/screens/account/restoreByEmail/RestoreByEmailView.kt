package io.forus.me.android.presentation.view.screens.account.restoreByEmail;

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface RestoreByEmailView : LRView<RestoreByEmailModel> {

    fun register(): io.reactivex.Observable<RestoreAccountByEmailRequest>

}