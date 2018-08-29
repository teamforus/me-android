package io.forus.me.android.presentation.view.screens.account.newaccount


import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.account.NewAccountRequest

sealed class AccountPartialChanges : PartialChange {

    data class NavigateToWelcomeScreen(val value: Boolean) : AccountPartialChanges()

}