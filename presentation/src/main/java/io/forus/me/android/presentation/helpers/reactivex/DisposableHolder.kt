package io.forus.me.android.presentation.helpers.reactivex

import io.reactivex.disposables.Disposable

class DisposableHolder{

    private val disposables: MutableList<Disposable> = mutableListOf()

    fun add(disposable: Disposable){
        disposables.add(disposable)
    }

    fun disposeAll(){
        disposables.forEach {
            it.dispose()
        }
    }
}