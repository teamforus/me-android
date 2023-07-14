package io.forus.me.android.presentation.view.base

import androidx.lifecycle.ViewModel

interface MViewModelProvider<T : ViewModel> {
    val viewModel: T
}