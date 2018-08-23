package io.forus.me.android.presentation.view.screens.account.assigndelegates.email;

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface RestoreByEmailView : LRView<RestoreByEmailModel> {

    fun register(): io.reactivex.Observable<String>

}