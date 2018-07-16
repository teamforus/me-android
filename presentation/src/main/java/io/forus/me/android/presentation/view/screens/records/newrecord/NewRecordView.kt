package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import kotlinx.android.synthetic.main.view_new_record_select_value.*

interface NewRecordView : LRView<NewRecordModel> {

    fun createRecord(): io.reactivex.Observable<Boolean>

    fun selectCategory(): io.reactivex.Observable<RecordCategory>

    fun selectType(): io.reactivex.Observable<RecordType>


    fun setValue(): io.reactivex.Observable<String>



}