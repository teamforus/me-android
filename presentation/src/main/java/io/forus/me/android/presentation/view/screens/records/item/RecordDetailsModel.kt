package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.view.screens.records.item.validators.ValidatorViewModel

data class RecordDetailsModel(
        val item: Record? = null,
        val validators: List<ValidatorViewModel> = emptyList(),
        val requestValidationError: Throwable? = null
)
{
    fun changeStatus(validatorId: Long) : RecordDetailsModel {
        val item = validators.find { it.id == validatorId }
        item?.status = ValidatorViewModel.Status.pending
        return copy()
    }
}