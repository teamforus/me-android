package io.forus.me.android.presentation.view.screens.records.item.qr

import io.forus.me.android.domain.models.records.Validation

data class RecordQRModel(
        val uuid: String? = null,
        val recordValidatedState: Validation.State? = null
)