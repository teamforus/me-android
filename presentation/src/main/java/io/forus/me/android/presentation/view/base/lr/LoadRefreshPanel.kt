package com.ocrv.ekasui.mrm.ui.loadRefresh

import io.reactivex.Observable

interface LoadRefreshPanel {
    fun retryClicks(): Observable<Any>
    fun refreshes(): Observable<Any>

    fun render(vs: LRViewState<*>)
}