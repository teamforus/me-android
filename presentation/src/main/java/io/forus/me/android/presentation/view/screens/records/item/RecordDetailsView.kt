package io.forus.me.android.presentation.view.screens.records.item

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.reactivex.Observable

interface RecordDetailsView : LRView<RecordDetailsModel> {

    fun requestValidation(): Observable<Long>

}