package io.forus.me.android.presentation.view.screens.records.newrecord

import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.domain.models.records.CreateRecordResponse
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.validators.SimpleValidator

sealed class NewRecordPartialChanges : PartialChange {

    data class CreateRecordStart(val model: NewRecordRequest) : NewRecordPartialChanges()

    data class CreateRecordEnd(val model: CreateRecordResponse) : NewRecordPartialChanges()

    data class CreateRecordError(val error: Throwable) : NewRecordPartialChanges()

    data class SelectCategory(val category: RecordCategory) : NewRecordPartialChanges()

    data class SelectValidator(val validator: SimpleValidator) : NewRecordPartialChanges()

    data class SelectType(val type: RecordType) : NewRecordPartialChanges()

    data class SetValue(val value: String) : NewRecordPartialChanges()

    class PreviousStep() : NewRecordPartialChanges()

    class NextStep() : NewRecordPartialChanges()
}