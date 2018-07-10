package com.ocrv.ekasui.mrm.ui.loadRefresh

sealed class LRPartialChange : PartialChange {
    object LoadingStarted : LRPartialChange()
    data class LoadingError(val t: Throwable) : LRPartialChange()
    object RefreshStarted : LRPartialChange()
    data class RefreshError(val t: Throwable) : LRPartialChange()
    data class InitialModelLoaded<out I>(val i: I) : LRPartialChange()
}