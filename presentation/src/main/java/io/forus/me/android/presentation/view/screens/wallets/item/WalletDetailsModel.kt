package io.forus.me.android.presentation.view.screens.wallets.item

import android.graphics.Bitmap
import io.forus.me.android.domain.models.wallets.Transaction
import io.forus.me.android.domain.models.wallets.Wallet

data class WalletDetailsModel(
        val item: Wallet? = null,
        val transactions: List<Transaction> = emptyList(),
        val creatingQrCode: Boolean = false,
        val qrCode: Bitmap? = null
)