package io.forus.me.android.presentation.view.screens.account.newaccount


import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.NewAccountRequest

sealed class NewAccountPartialChanges : PartialChange {


    data class RegisterStart(val model: NewAccountRequest) : NewAccountPartialChanges()

    data class RegisterEnd(val model: NewAccountRequest) : NewAccountPartialChanges()

}