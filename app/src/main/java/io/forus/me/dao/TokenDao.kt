package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.forus.me.entities.Token

/**
 * Created by martijn.doornik on 05/03/2018.
 */

@Dao
interface TokenDao {

    @Delete
    fun delete(token: Token)

    @Query("SELECT * FROM `token` WHERE `identity` = :arg0")
    fun getTokens(identity: String): List<Token>

    @Query("SELECT * FROM `token` WHERE `address` = :arg0 AND `identity` = :arg1")
    fun getTokenByAddressByIdentity(address:String, identity: String): Token

    @Insert
    fun insert(token: Token)

    @Update
    fun update(token: Token)
}