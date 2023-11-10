package io.forus.me.android.presentation.view.base.lr

import io.reactivex.Observable

interface LoadRefreshPanel {
    fun retryClicks(): Observable<Any>
    fun refreshes(): Observable<Any>

    fun render(vs: LRViewState<*>)
}