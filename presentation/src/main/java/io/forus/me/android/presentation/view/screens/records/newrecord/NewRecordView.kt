package io.forus.me.android.presentation.view.screens.records.newrecord

import com.ocrv.ekasui.mrm.ui.loadRefresh.LRView
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.validators.SimpleValidator

interface NewRecordView : LRView<NewRecordModel> {
    companion object {
        const val NUM_PAGES = 4
    }

    fun onBackPressed(): Boolean

    fun previousStep(): io.reactivex.Observable<Boolean>

    fun nextStep(): io.reactivex.Observable<Boolean>

    fun submit(): io.reactivex.Observable<Boolean>

    fun selectCategory(): io.reactivex.Observable<RecordCategory>

    fun selectType(): io.reactivex.Observable<RecordType>

    fun selectValidator(): io.reactivex.Observable<SimpleValidator>

    fun setValue(): io.reactivex.Observable<String>
}