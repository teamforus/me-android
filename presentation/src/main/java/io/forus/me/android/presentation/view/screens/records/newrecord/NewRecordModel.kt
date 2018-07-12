package io.forus.me.android.presentation.view.screens.records.newrecord

import io.forus.me.android.domain.models.records.NewRecordRequest

data class NewRecordModel(
        val item: NewRecordRequest = NewRecordRequest(),
        val sendingCreateRecord: Boolean = false
) {
}