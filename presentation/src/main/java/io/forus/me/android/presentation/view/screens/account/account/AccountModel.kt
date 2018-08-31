package io.forus.me.android.presentation.view.screens.account.account;

import io.forus.me.android.domain.models.account.Account


data class AccountModel(
        val account: Account? = null,
        val navigateToWelcome: Boolean = false
        )