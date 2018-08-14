package io.forus.me.android.presentation.view.screens.records.item

import android.graphics.Bitmap
import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class RecordDetailsPartialChanges : PartialChange {

    data class CreateQrCodeStart(val uuid: String) : RecordDetailsPartialChanges()

    data class CreateQrCodeEnd(val bitmap: Bitmap?) : RecordDetailsPartialChanges()

}