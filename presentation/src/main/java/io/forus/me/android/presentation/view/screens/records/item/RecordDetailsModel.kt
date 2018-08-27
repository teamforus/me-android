package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.validators.SimpleValidator

data class RecordDetailsModel(
        val item: Record? = null,
        val validators: List<SimpleValidator> = emptyList()
)