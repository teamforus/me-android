package io.forus.me.android.domain.models.records

import io.forus.me.android.domain.models.validators.SimpleValidator

data class NewRecordRequest(
        val recordType: RecordType? = null,
        val category: RecordCategory? = null,
        val validators: List<SimpleValidator>? = null,
        val value: String = "",
        val order: Long = 0
)
{
    fun selectValidator(validator: SimpleValidator): NewRecordRequest{
        val selectedValidators: MutableList<SimpleValidator> = mutableListOf()
        if(validators != null) selectedValidators.addAll(validators)
        if(selectedValidators.contains(validator)) selectedValidators.remove(validator) else selectedValidators.add(validator)
        return copy(validators = selectedValidators)
    }
}