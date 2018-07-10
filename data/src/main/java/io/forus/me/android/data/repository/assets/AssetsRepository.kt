package io.forus.me.android.data.repository.assets

import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.domain.models.assets.Type
import io.forus.me.android.domain.models.currency.Currency
import io.forus.me.android.domain.models.wallets.Wallet
import io.reactivex.Observable
import io.reactivex.Single

class AssetsRepository() : io.forus.me.android.domain.repository.assets.AssetsRepository {

    override fun getAssets(): Observable<List<Asset>> {
        val wallets : MutableList<Asset> = mutableListOf()

        for (i in 1..100){
            wallets.add(Asset("Groningen", "9712 CP Groningen", Type("KANTOOR")))
            wallets.add(Asset("Mercedes G-class", "9731 EU", Type("KFS")))

        }

        return Single.just(wallets).toObservable().
                map {
                    it.toList()
                }
    }


}