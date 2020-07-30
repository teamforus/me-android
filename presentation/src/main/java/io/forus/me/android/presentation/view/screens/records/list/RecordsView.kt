package io.forus.me.android.presentation.view.screens.records.list

import io.forus.me.android.presentation.view.base.lr.LRView
import io.reactivex.Observable

/**
 * Created by pavelpantuhov on 31.10.2017.
 */


interface RecordsView : LRView<RecordsModel>{
    fun records(): Observable<Long>
    fun archives(): Observable<Long>
}