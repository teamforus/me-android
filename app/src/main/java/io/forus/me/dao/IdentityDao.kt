package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.forus.me.entities.Identity

/**
 * Created by martijn.doornik on 05/03/2018.
 */

@Dao
interface IdentityDao {

    @Query("SELECT * FROM `identity` WHERE `id` = :arg0")
    fun getIdentityById(identityId: Long): Identity

    @Query("SELECT COUNT(*) FROM `identity`")
    fun getIdentityCount(): Int

    @Query("SELECT COUNT(*) FROM `identity` WHERE `name` = :arg0")
    fun getIdentityCount(name:String): Int

    @Query("SELECT * FROM `Identity`")
    fun getIdentities(): LiveData<List<Identity>>

    @Insert
    fun create(identity: Identity): Long

    @Delete
    fun delete(identity: Identity)
}