package io.forus.me.android.presentation.view.screens.account.restoreByEmail


import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest

sealed class RestoreByEmailPartialChanges : PartialChange {


    data class RegisterStart(val model: RestoreAccountByEmailRequest) : RestoreByEmailPartialChanges()

    data class RegisterEnd(val model: RestoreAccountByEmailRequest) : RestoreByEmailPartialChanges()

}