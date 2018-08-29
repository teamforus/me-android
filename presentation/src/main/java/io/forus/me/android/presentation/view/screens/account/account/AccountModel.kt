package io.forus.me.android.presentation.view.screens.account.newaccount;

import io.forus.me.android.domain.models.account.Account


data class AccountModel(
        val account: Account? = null,
        val navigateToWelcome: Boolean = false
        )