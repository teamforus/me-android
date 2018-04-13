package io.forus.me.services

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import io.forus.me.dao.*
import io.forus.me.entities.*
import io.forus.me.helpers.ThreadHelper

@Database(entities = arrayOf(
        Asset::class,
        Identity::class,
        Record::class,
        Service::class,
        Token::class
        ), version = 9)
abstract class DatabaseService: RoomDatabase() {

    abstract fun assetDao(): AssetDao
    abstract fun identityDao(): IdentityDao
    abstract fun recordDao(): RecordDao
    abstract fun serviceDao(): ServiceDao
    abstract fun tokenDao(): TokenDao

    fun delete(identity: Identity) {
        identityThread.postTask(Runnable { identityDao().delete(identity) })
    }

    fun delete(record: Record) {
        recordThread.postTask(Runnable { recordDao().delete(record) })
    }

    fun delete(token: Token) {
        tokenThread.postTask(Runnable { tokenDao().delete(token) })
    }

    fun insert(asset:Asset) {
        assetThread.postTask(Runnable { assetDao().insert(asset) })
    }

    fun insert(identity: Identity): Long {
        var newId:Long = -1
        identityThread.postTask(Runnable { newId = identityDao().create(identity) })
        return newId
    }

    fun insert(record: Record) {
        recordThread.postTask(Runnable {recordDao().insert(record)})
    }

    fun insert(service:Service) {
        serviceThread.postTask(Runnable {serviceDao().insert(service)})
    }

    fun insert(token: Token) {
        tokenThread.postTask(Runnable { tokenDao().insert(token) })
    }

    fun update(asset:Asset) {
        assetThread.postTask(Runnable { assetDao().update(asset) })
    }


    fun update(record: Record) {
        recordThread.postTask(Runnable {recordDao().update(record)})
    }

    fun update(service:Service) {
        serviceThread.postTask(Runnable {serviceDao().update(service)})
    }

    fun update(token: Token) {
        tokenThread.postTask(Runnable { tokenDao().update(token) })
    }

    companion object {

        private val assetThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.ASSET_THREAD)
        private val identityThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.IDENTITY_THREAD)
        private val mainThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.MAIN_THREAD)
        private val recordThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.RECORD_THREAD)
        private val serviceThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.SERVICE_THREAD)
        private val tokenThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.TOKEN_THREAD)

        var database: DatabaseService? = null
        val ready:Boolean
            get() = database != null

        fun inject(context: Context): DatabaseService {
            if (database == null) {
                synchronized(DatabaseService::class) {
                    database = Room.databaseBuilder(context.applicationContext,
                            DatabaseService::class.java, "me_client_architecture")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return database!!
        }

        val inject: DatabaseService
                get() {
                    if (database == null) {
                        throw RuntimeException("Database not yet initiated")
                    }
                    return database!!
                }

        fun prepare(context: Context): DatabaseService {
            mainThread.postTask(Runnable {
                this.inject(context)
            })
            while (!this.ready) {}
            return this.inject
        }

    }
}