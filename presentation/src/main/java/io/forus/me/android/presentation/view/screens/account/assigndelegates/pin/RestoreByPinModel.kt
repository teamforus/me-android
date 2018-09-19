package io.forus.me.android.presentation.view.screens.account.assigndelegates.pin

import io.forus.me.android.domain.models.account.RequestDelegatesPinModel

data class RestoreByPinModel(
        val item: RequestDelegatesPinModel? = null,
        val isPinConfirmed: Boolean = false
)