package io.forus.me.android.presentation.view.screens.account.assigndelegates

import io.forus.me.android.domain.models.account.RequestDelegatesPinModel

data class AssignDelegatesAccountModel(
        val item: RequestDelegatesPinModel? = null,
        val isPinConfirmed: Boolean = false
)