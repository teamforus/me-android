package io.forus.me.android.presentation.view.screens.records.item

import android.graphics.Bitmap
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.Validator

data class RecordDetailsModel(
        val item: Record? = null,
        val uuid: String? = null,
        val creatingQrCode: Boolean = false,
        val qrCode: Bitmap? = null,
        val validators: List<Validator> = emptyList()
)