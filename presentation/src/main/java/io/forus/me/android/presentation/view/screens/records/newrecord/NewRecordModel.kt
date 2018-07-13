package io.forus.me.android.presentation.view.screens.records.newrecord

import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType

data class NewRecordModel(
        val item: NewRecordRequest = NewRecordRequest(),
        val sendingCreateRecord: Boolean = false,
        val types: List<RecordType> = emptyList(),
        val categories: List<RecordCategory> = emptyList()
) {
}