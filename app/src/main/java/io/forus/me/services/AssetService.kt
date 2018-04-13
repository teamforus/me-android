package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.entities.Asset
import io.forus.me.services.base.BaseService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
class AssetService : BaseService() {


    companion object {
        fun addAsset(asset: Asset) {
            DatabaseService.inject.insert(asset)
        }

        fun getAssetsByIdentity(identity:String): List<Asset>? {
            return DatabaseService.database?.assetDao()?.getAssets(identity)
        }

        fun getAssetByAddressByIdentity(address:String, identity:String): Asset? {
            return DatabaseService.database?.assetDao()?.getAssetByAddressByIdentity(address, identity)
        }
    }
}