package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.forus.me.android.domain.models.records.NewRecordRequest

interface NewRecordView : LRView<NewRecordModel> {

    fun createRecord(): io.reactivex.Observable<NewRecordRequest>

}