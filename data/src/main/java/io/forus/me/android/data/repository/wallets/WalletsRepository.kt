package io.forus.me.android.data.repository.wallets

import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable
import io.reactivex.Single

class WalletsRepository() : io.forus.me.android.domain.repository.wallets.WalletsRepository {

    override fun getWallets(): Observable<List<Wallet>> {
        val wallets : MutableList<Wallet> = mutableListOf()

        for (i in 1..100){
            wallets.add(Wallet("Ethereum Wallet", 2.568396f, Currency("Eth", "https://cdn.worldvectorlogo.com/logos/ethereum.svg")))
            wallets.add(Wallet("BAT Wallet", 67843.767f, Currency("Bat", "https://cdn.worldvectorlogo.com/logos/ethereum.svg")))

        }

        return Single.just(wallets).toObservable().
                map {
                    it.toList()
                }
    }
}