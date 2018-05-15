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

    @Query("SELECT * FROM `token` WHERE `address` = :address")
    fun getLiveToken(address: String): LiveData<Token>

    @Query("SELECT * FROM `token` WHERE `address` = :address")
    fun getToken(address: String): Token

    @Query("SELECT * FROM `token` WHERE `identity` = :identity")
    fun getTokens(identity: String): List<Token>

    @Query("SELECT * FROM `token` WHERE `identity` = :identity")
    fun getTokensLiveData(identity: String): LiveData<List<Token>>

    @Query("SELECT * FROM `token` WHERE `address` = :address AND `identity` = :identity")
    fun getTokenByAddressByIdentity(address:String, identity: String): Token

    @Insert
    fun insert(token: Token)

    @Update
    fun update(token: Token)
}