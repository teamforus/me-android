package io.forus.me.android.domain.models.records

data class NewRecordRequest(
        val recordType: RecordType? = null,
        val category: RecordCategory? = null,
        val value: String = "",
        val order: Long = 0
) {

}