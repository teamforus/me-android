package io.forus.me.android.data.entity.database

class DatabaseManager private constructor() : Database {

    init {
        println("This ($this) is a singleton")
    }
    private object Holder {
        val INSTANCE = DatabaseManager()
    }
    companion object {
        val instance: DatabaseManager by lazy { Holder.INSTANCE }
    }

    lateinit var database: Database

    override fun exists(): Boolean {
        return database.exists()
    }

    override fun isOpen(): Boolean {
        return database.isOpen
    }

    override fun open(pin: String): Boolean {
        return database.open(pin)
    }

    override fun close(): Boolean {
        return database.close()
    }

    override fun delete(): Boolean {
        return database.delete()
    }
}
