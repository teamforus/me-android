package io.forus.me.android.presentation.view.screens.account.assigndelegates.qr

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel

data class RestoreByQRModel(
        val item: RequestDelegatesQrModel? = null,
        val isQrConfirmed: Boolean = false
)