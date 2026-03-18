package io.forus.me.android.presentation.view.screens.records.item.qr

import io.forus.me.android.domain.models.records.Validation
import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class RecordQRPartialChanges : PartialChange {

    data class RecordValidated(val state: Validation.State) : RecordQRPartialChanges()

}