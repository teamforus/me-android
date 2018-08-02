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

        wallets.add(Asset("Amsterdam", "1097AS Amsterdam", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFNzSV5SVK7J-HTveWEuPlQgiu1YyEUqOzF_ygG4eUZnqr6Wfe",Type("Аppartement")))
        wallets.add(Asset("Fiat multipla", "9731 EU", "http://autoveilingalbergen.nl/wp-content/uploads/2016/07/placeholder-auto-icon.png",Type("Auto")))

        return Single.just(wallets).toObservable().
                map {
                    it.toList()
                }
    }


}