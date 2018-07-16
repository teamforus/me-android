package io.forus.me.android.presentation.view.screens.records.newrecord

import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.models.ValidationResult

data class NewRecordModel(
        val item: NewRecordRequest = NewRecordRequest(),
        val sendingCreateRecord: Boolean = false,
        val sendingCreateRecordError: Throwable? = null,
        val types: List<RecordType> = emptyList(),
        val categories: List<RecordCategory> = emptyList()
) {


    val  validationResult: ValidationResult
        get() {
            var valid = false
            var error = ""
            when {
                sendingCreateRecord -> error =  ("Request in progress")
                item.category == null -> error = ("Please select category")
                item.recordType == null -> error =  ("Please select type")
                item.value.isEmpty() -> error =  ("Value is not valid")
                else -> valid = true
            }
            return ValidationResult(valid, error)
        }



}