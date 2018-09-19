package io.forus.me.android.presentation.view.screens.wallets.item

import android.graphics.Bitmap
import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class WalletDetailsPartialChanges : PartialChange {

    data class CreateQrCodeStart(val uuid: String) : WalletDetailsPartialChanges()

    data class CreateQrCodeEnd(val bitmap: Bitmap?) : WalletDetailsPartialChanges()

}