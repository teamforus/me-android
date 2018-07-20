package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType

sealed class NewRecordPartialChanges : PartialChange {

    data class CreateRecordStart(val model: NewRecordRequest) : NewRecordPartialChanges()

    data class CreateRecordEnd(val model: NewRecordRequest) : NewRecordPartialChanges()

    data class CreateRecordError(val error: Throwable) : NewRecordPartialChanges()

    data class SelectCategory(val category: RecordCategory) : NewRecordPartialChanges()

    data class SelectType(val type: RecordType) : NewRecordPartialChanges()

    data class SetValue(val value: String) : NewRecordPartialChanges()

    class PreviousStep() : NewRecordPartialChanges()

    class NextStep() : NewRecordPartialChanges()
}