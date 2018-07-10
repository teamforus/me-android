package io.forus.me.android.domain.repository.wallets

import io.forus.me.android.domain.models.account.NewAccountRequest
import io.forus.me.android.domain.models.account.RequestDelegatesQrModel
import io.forus.me.android.domain.models.account.RestoreAccountByEmailRequest
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable

interface WalletsRepository {

    fun getWallets(): Observable<List<Wallet>>


}
