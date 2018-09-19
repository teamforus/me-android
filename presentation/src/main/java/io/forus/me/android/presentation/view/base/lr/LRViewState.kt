package io.forus.me.android.presentation.view.base.lr

data class LRViewState<out M>(
        val loading: Boolean,
        val loadingError: Throwable?,
        val canRefresh: Boolean,
        val refreshing: Boolean,
        val refreshingError: Throwable?,
        val closeScreen: Boolean,
        val model: M
)