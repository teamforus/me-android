package io.forus.me.android.domain.repository.wallets

import io.forus.me.android.domain.models.wallets.Transaction
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable

interface WalletsRepository {

    fun getWallets(): Observable<List<Wallet>>

    fun getWallet(walletId: Long): Observable<Wallet>

    fun getTransactions(walletId: Long): Observable<List<Transaction>>

    fun getTransaction(transactionId: String): Observable<Transaction>
}
