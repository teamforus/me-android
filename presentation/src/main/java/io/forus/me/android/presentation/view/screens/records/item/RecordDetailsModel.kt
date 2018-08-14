package io.forus.me.android.presentation.view.screens.records.item

import android.graphics.Bitmap
import io.forus.me.android.domain.models.records.Record

data class RecordDetailsModel(
        val item: Record? = null,
        val uuid: String? = null,
        val creatingQrCode: Boolean = false,
        val qrCode: Bitmap? = null
)