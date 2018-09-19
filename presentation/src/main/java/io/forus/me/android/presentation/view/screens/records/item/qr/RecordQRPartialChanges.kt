package io.forus.me.android.presentation.view.screens.records.item.qr

import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.records.Validation

sealed class RecordQRPartialChanges : PartialChange {

    data class RecordValidated(val state: Validation.State) : RecordQRPartialChanges()

}