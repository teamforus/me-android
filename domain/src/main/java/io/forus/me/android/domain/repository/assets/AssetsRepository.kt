package io.forus.me.android.domain.repository.assets

import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest
import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable

interface AssetsRepository {

    fun getAssets(): Observable<List<Asset>>


}
