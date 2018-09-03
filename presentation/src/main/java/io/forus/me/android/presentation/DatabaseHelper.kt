package io.forus.me.android.presentation

import android.content.Context
import io.forus.me.android.data.entity.database.DaoMaster
import io.forus.me.android.data.entity.database.DaoSession
import io.forus.me.android.data.entity.database.Database
import io.forus.me.android.data.entity.database.DatabaseManager
import io.forus.me.android.presentation.helpers.SHA256
import io.forus.me.android.presentation.internal.Injection

class DatabaseHelper(private val context: Context): Database{

    init {
        DatabaseManager.instance.database = this
    }

    companion object {
        const val DB_NAME = "main-db"
        const val SALT = "djawdjawiodjawiodjawidjaw1273682193782193"
    }

    private var db: org.greenrobot.greendao.database.Database? = null
    private var daoSession: DaoSession? = null
        set(value) {
            field = value
            if(Injection.instance.daoSession != daoSession) Injection.instance.daoSession = daoSession
        }

    override fun refresh() {
        Injection.instance.accessTokenUpdated()
    }

    override fun exists(): Boolean{
        val dbFile = context.getDatabasePath(DB_NAME)
        return dbFile.exists()
    }

    override fun isOpen(): Boolean{
        return daoSession != null
    }

    override fun open(pin: String): Boolean{
        return try {
            close()
            val helper = DaoMaster.DevOpenHelper(context, DB_NAME)
            db = helper.getEncryptedWritableDb((pin+SALT).SHA256())
            daoSession = DaoMaster(db).newSession()
            true
        }
        catch (e: Exception){
            false
        }
    }

    override fun checkPin(pin: String): Boolean {
        return try {
            val helper = DaoMaster.DevOpenHelper(context, DB_NAME)
            val db = helper.getEncryptedReadableDb((pin+SALT).SHA256())
            db?.close()
            true
        }
        catch (e: Exception){
            false
        }
    }

    override fun close(): Boolean{
        db?.close()
        db = null
        daoSession = null
        return true
    }

    override fun delete(): Boolean{
        return if(exists()){
            close()
            context.deleteDatabase(DB_NAME)
        }
        else false
    }
}