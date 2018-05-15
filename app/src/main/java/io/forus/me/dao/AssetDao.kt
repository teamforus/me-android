package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.forus.me.entities.Asset

/**
 * Created by martijn.doornik on 05/03/2018.
 */

@Dao
interface AssetDao {

    @Delete
    fun delete(asset: Asset)

    @Query("SELECT * FROM `asset` WHERE `address` = :address")
    fun getAsset(address: String): Asset

    @Query("SELECT * FROM `asset` WHERE `identity` = :identity")
    fun getAssets(identity:String): List<Asset>

    @Query("SELECT * FROM `asset` WHERE `identity` = :identity")
    fun getAssetsLiveData(identity:String): LiveData<List<Asset>>

    @Query("SELECT * FROM `asset` WHERE `address` = :address AND `identity` = :identity")
    fun getAssetByAddressByIdentity(address:String, identity: String): Asset

    @Query("SELECT * FROM `asset` WHERE `address` = :address")
    fun getLiveAsset(address: String): LiveData<Asset>

    @Insert
    fun insert(asset: Asset)

    @Update
    fun update(asset: Asset)
}