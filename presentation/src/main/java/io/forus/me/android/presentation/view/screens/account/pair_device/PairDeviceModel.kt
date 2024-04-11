package io.forus.me.android.presentation.view.screens.account.pair_device

import io.forus.me.android.domain.models.account.RequestDelegatesPinModel
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel

data class PairDeviceModel(


        val item: RequestDelegatesPinModel? = null,
        val isPinConfirmed: Boolean = false


)