package io.forus.me.android.presentation.view.screens.records.list

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.records.Record

data class RecordsModel(
        val items: List<Record> = emptyList(),
        val archives: List<Record> = emptyList(),
        val requestError: Throwable? = null
        )