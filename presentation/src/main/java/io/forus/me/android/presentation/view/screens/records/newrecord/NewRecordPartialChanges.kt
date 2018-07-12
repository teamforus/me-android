package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange
import io.forus.me.android.domain.models.records.NewRecordRequest

sealed class NewRecordPartialChanges : PartialChange {


    data class CreateRecordStart(val model: NewRecordRequest) : NewRecordPartialChanges()

    data class CreateRecordEnd(val model: NewRecordRequest) : NewRecordPartialChanges()

}