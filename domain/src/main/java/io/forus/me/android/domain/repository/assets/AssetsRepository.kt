package io.forus.me.android.domain.repository.assets

import io.forus.me.android.domain.models.assets.Asset
import io.reactivex.Observable

interface AssetsRepository {

    fun getAssets(): Observable<List<Asset>>


}
