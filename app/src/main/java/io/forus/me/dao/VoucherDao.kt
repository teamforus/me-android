package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.forus.me.entities.Voucher

/**
 * Created by martijn.doornik on 05/03/2018.
 */

@Dao
interface VoucherDao {

    @Delete
    fun delete(voucher: Voucher)

    @Query("SELECT * FROM `voucher` WHERE `address` = :address")
    fun getLiveVoucher(address: String): LiveData<Voucher>

    @Query("SELECT * FROM `voucher` WHERE `address` = :address")
    fun getVoucher(address: String): Voucher

    @Query("SELECT * FROM `voucher` WHERE `identity` = :identity")
    fun getVouchers(identity: String): List<Voucher>

    @Query("SELECT * FROM `voucher` WHERE `identity` = :identity")
    fun getVouchersLiveData(identity: String): LiveData<List<Voucher>>

    @Query("SELECT * FROM `voucher` WHERE `address` = :address AND `identity` = :identity")
    fun getVoucherByAddressByIdentity(address:String, identity: String): Voucher

    @Insert
    fun insert(voucher: Voucher)

    @Update
    fun update(voucher: Voucher)
}