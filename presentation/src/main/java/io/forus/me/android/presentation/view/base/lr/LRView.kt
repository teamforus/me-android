package com.ocrv.ekasui.mrm.ui.loadRefresh

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface LRView<in M> : MvpView {
    fun retry(): Observable<Any>
    fun refresh(): Observable<Any>
    fun updateData(): Observable<Any>
    fun render(vs: LRViewState<M>)

}