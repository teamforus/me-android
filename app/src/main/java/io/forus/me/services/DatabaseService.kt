package io.forus.me.services

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import io.forus.me.dao.*
import io.forus.me.entities.*
import io.forus.me.helpers.ThreadHelper

@Database(entities = [
    (Asset::class),
    (Identity::class),
    (Record::class),
    (Voucher::class),
    (Token::class)
], version = 13)
abstract class DatabaseService: RoomDatabase() {

    abstract fun assetDao(): AssetDao
    abstract fun identityDao(): IdentityDao
    abstract fun recordDao(): RecordDao
    abstract fun voucherDao(): VoucherDao
    abstract fun tokenDao(): TokenDao

    companion object {
        private val mainThread: ThreadHelper.DataThread
            get() = ThreadHelper.dispense(ThreadHelper.MAIN_THREAD)

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