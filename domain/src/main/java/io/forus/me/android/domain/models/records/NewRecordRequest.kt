package io.forus.me.android.domain.models.records

data class NewRecordRequest(
        val recordType: RecordType? = null,
        val category: RecordCategory? = null,
        val validators: List<Validator>? = null,
        val value: String = "",
        val order: Long = 0
)
{
    fun selectValidator(validator: Validator): NewRecordRequest{
        val selectedValidators: MutableList<Validator> = mutableListOf()
        if(validators != null) selectedValidators.addAll(validators)
        if(selectedValidators.contains(validator)) selectedValidators.remove(validator) else selectedValidators.add(validator)
        return copy(validators = selectedValidators)
    }
}