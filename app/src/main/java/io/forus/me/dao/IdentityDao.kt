package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.forus.me.entities.Identity

/**
 * Created by martijn.doornik on 05/03/2018.
 */

@Dao
interface IdentityDao {

    @Query("SELECT * FROM `identity` WHERE `address` = :address")
    fun getIdentity(address:String): Identity

    @Query("SELECT * FROM `identity` WHERE `id` = :identityId")
    fun getIdentityById(identityId: Long): Identity

    @Query("SELECT COUNT(*) FROM `identity`")
    fun getIdentityCount(): Int

    @Query("SELECT COUNT(*) FROM `identity` WHERE `name` = :name")
    fun getIdentityCount(name:String): Int

    @Query("SELECT * FROM `Identity`")
    fun getIdentityLiveData(): LiveData<List<Identity>>

    @Query("SELECT * FROM `Identity`")
    fun getIdentities(): List<Identity>

    @Query("SELECT * FROM `identity` WHERE `address` = :address")
    fun getLiveIdentity(address:String): LiveData<Identity>

    @Insert
    fun create(identity: Identity): Long

    @Delete
    fun delete(identity: Identity)

    @Update
    fun update(identity: Identity)
}