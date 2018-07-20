package io.forus.me.android.presentation.view.screens.records.newrecord

import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.models.ValidationResult

data class NewRecordModel(
        val item: NewRecordRequest = NewRecordRequest(),
        val currentStep: Int = 0,
        val sendingCreateRecord: Boolean = false,
        val sendingCreateRecordError: Throwable? = null,
        val types: List<RecordType> = emptyList(),
        val categories: List<RecordCategory> = emptyList()
) {

    val buttonIsActive: Boolean
        get() {
            var active = when {
                sendingCreateRecord -> false
                currentStep >= 0 && item.category == null -> false
                currentStep >= 1 && item.recordType == null -> false
                currentStep >= 2 && item.value.isEmpty() -> false
                else -> true
            }
            return active
        }

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