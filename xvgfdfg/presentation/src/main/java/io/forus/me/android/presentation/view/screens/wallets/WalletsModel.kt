package io.forus.me.android.presentation.view.screens.wallets

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.wallets.Wallet

data class WalletsModel(
        val items: List<Wallet> = emptyList()
        )