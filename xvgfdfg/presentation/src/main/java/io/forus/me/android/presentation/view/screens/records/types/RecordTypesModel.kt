package io.forus.me.android.presentation.view.screens.records.types

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.RecordCategory

data class RecordTypesModel(
        //val items: List<RecordCategory> = emptyList()
        val items: List<io.forus.me.android.domain.models.records.RecordType> = emptyList()
        )