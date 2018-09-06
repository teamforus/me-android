package io.forus.me.android.presentation.view.screens.account.account


import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class AccountPartialChanges : PartialChange {

    data class NavigateToWelcomeScreen(val value: Boolean) : AccountPartialChanges()

}