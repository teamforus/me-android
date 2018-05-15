package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.entities.Asset
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.base.BaseService
import io.forus.me.services.base.EthereumItemService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
class AssetService : EthereumItemService<Asset>() {

    override fun add(item: Asset) {
        addAsset(item)
    }

    override fun delete(item: Asset) {
        deleteAsset(item)
    }

    override fun getItem(address: String): Asset {
        return DatabaseService.inject.assetDao().getAsset(address)
    }

    override fun getList(identity: String): List<Asset> {
        return DatabaseService.inject.assetDao().getAssets(identity)
    }

    override fun getLiveItem(address: String): LiveData<Asset> {
        return DatabaseService.inject.assetDao().getLiveAsset(address)
    }

    override fun getLiveData(identity: String): LiveData<List<Asset>> {
        return DatabaseService.inject.assetDao().getAssetsLiveData(identity)
    }

    override fun getThread(): ThreadHelper.DataThread {
        return thread
    }

    override fun update(item: Asset) {
        updateAsset(item)
    }

    companion object {
        private val thread: ThreadHelper.DataThread
                get() = ThreadHelper.dispense(ThreadHelper.ASSET_THREAD)

        fun addAsset(asset: Asset) {
            thread.postTask(Runnable { DatabaseService.inject.assetDao().insert(asset) })
        }

        fun deleteAsset(asset:Asset) {
            thread.postTask(Runnable { DatabaseService.inject.assetDao().delete(asset) })
        }

        fun getAssetsByIdentity(identity:String): List<Asset>? {
            return DatabaseService.database?.assetDao()?.getAssets(identity)
        }

        fun getAssetByAddressByIdentity(address:String, identity:String): Asset? {
            return DatabaseService.database?.assetDao()?.getAssetByAddressByIdentity(address, identity)
        }

        fun updateAsset(asset: Asset) {
            thread.postTask(Runnable { DatabaseService.inject.assetDao().update(asset) })
        }
    }
}