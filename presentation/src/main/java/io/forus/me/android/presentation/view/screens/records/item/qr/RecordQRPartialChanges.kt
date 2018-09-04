package io.forus.me.android.presentation.view.screens.records.item.qr

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.Validation

sealed class RecordQRPartialChanges : PartialChange {

    data class RecordValidated(val state: Validation.State) : RecordQRPartialChanges()

}