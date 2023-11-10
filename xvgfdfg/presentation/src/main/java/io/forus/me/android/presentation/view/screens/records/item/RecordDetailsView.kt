package io.forus.me.android.presentation.view.screens.records.item

import io.forus.me.android.presentation.view.base.lr.LRView
import io.reactivex.Observable

interface RecordDetailsView : LRView<RecordDetailsModel> {

    fun requestValidation(): Observable<Long>

    fun deleteRecord(): Observable<Long>

    fun editRecord(): Observable<Long>

}