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

    @Query("SELECT * FROM `asset` WHERE `identity` = :arg0")
    fun getAssets(identity:String): List<Asset>

    @Query("SELECT * FROM `asset` WHERE `address` = :arg0 AND `identity` = :arg1")
    fun getAssetByAddressByIdentity(address:String, identity: String): Asset

    @Insert
    fun insert(asset: Asset)

    @Update
    fun update(asset: Asset)
}