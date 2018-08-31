package io.forus.me.android.presentation.view.screens.account.account


import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class AccountPartialChanges : PartialChange {

    data class NavigateToWelcomeScreen(val value: Boolean) : AccountPartialChanges()

}