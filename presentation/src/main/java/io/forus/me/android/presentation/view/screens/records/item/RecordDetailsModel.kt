package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.validators.SimpleValidator

data class RecordDetailsModel(
        val item: Record? = null,
        val validators: List<SimpleValidator> = emptyList(),
        val requestValidationError: Throwable? = null
)
{
    fun changeStatus(validatorId: Long) : RecordDetailsModel {
        val item = validators.find { it.id == validatorId }
        item?.status = SimpleValidator.Status.pending
        return copy()
    }
}