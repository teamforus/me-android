package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.Validator

data class RecordDetailsModel(
        val item: Record? = null,
        val validators: List<Validator> = emptyList()
)