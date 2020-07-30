package io.forus.me.android.presentation.view.screens.records.list


import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsPartialChanges

sealed class RecordsPartialChanges : PartialChange {


    data class RequestRecordsSuccess(val recordsR: List<Record>): RecordsPartialChanges()

    data class RequestArchivesSuccess(val archiveR: List<Record>): RecordsPartialChanges()

    data class RequestError(val error: Throwable): RecordsPartialChanges()
}