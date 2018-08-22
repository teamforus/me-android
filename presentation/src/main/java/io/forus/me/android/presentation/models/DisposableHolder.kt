package io.forus.me.android.presentation.models

import io.reactivex.disposables.Disposable

class DisposableHolder{

    val disposables: MutableList<Disposable> = mutableListOf()

    fun add(disposable: Disposable){
        disposables.add(disposable)
    }

    fun disposeAll(){
        disposables.forEach {
            it.dispose()
        }
    }
}