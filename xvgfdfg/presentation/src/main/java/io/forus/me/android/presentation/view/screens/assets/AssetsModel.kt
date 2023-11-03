package io.forus.me.android.presentation.view.screens.assets

import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.assets.Asset

data class AssetsModel(
        val items: List<Asset> = emptyList()
        )