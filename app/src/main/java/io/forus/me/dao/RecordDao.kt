package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.forus.me.entities.Record
import io.forus.me.entities.RecordCategory

/**
 * Created by martijn.doornik on 28/02/2018.
 */
@Dao
interface RecordDao {

    @Delete
    fun delete(record: Record)

    @Query("SELECT * FROM record WHERE `address` = :address")
    fun getLiveRecord(address: String): LiveData<Record>

    @Query("SELECT * FROM record WHERE `address` = :address")
    fun getRecord(address: String): Record

    @Query("SELECT * FROM record WHERE `identity` = :identity")
    fun getRecords(identity: String): List<Record>

    @Query("SELECT * FROM record WHERE `recordCategoryId` = :category AND `identity` = :identity")
    fun getRecordsFromCategoryAndIdentity(category: Int, identity: String): LiveData<List<Record>>

    @Query("SELECT * FROM record WHERE `identity` = :identity")
    fun getRecordsLiveData(identity: String): LiveData<List<Record>>

    @Insert
    fun insert(record: Record)

    @Update
    fun update(record: Record)
}