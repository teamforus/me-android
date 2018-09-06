package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class RecordDetailsPartialChanges : PartialChange {

    data class RequestValidationStart(val validatorId: Long): RecordDetailsPartialChanges()

    data class RequestValidationEnd(val validatorId: Long): RecordDetailsPartialChanges()

    data class RequestValidationError(val error: Throwable): RecordDetailsPartialChanges()

}