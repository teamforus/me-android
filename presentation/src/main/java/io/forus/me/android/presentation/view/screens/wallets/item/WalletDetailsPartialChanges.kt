package io.forus.me.android.presentation.view.screens.wallets.item

import android.graphics.Bitmap
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class WalletDetailsPartialChanges : PartialChange {

    data class CreateQrCodeStart(val uuid: String) : WalletDetailsPartialChanges()

    data class CreateQrCodeEnd(val bitmap: Bitmap?) : WalletDetailsPartialChanges()

}