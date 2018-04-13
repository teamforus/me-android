package io.forus.me.helpers

import io.forus.me.entities.*
import io.forus.me.entities.base.EthereumItem
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.RecordService
import org.json.JSONObject

/**
 * Created by martijn.doornik on 06/03/2018.
 */
class JsonHelper {
    class Keys {
        companion object {
            val ADDRESS: String = "address"
            val ASSET: String = "asset"
            val CATEGORY: String = "category"
            val NAME: String = "name"
            val KEY: String = "key"
            val RECORD: String = "record"
            val SERVICE: String = "service"
            val TOKEN: String = "token"
            val TYPE: String = "type"
        }

    }

    companion object {

        fun fromEthereumItem(item: EthereumItem): JSONObject {
            val json:JSONObject
            when (item) {
                is WalletItem -> json = fromWalletItem(item)
                is Record -> {
                    json = JSONObject()
                    json.put(Keys.KEY, item.key)
                    json.put(Keys.TYPE, Keys.RECORD)

                }
                else -> json = JSONObject()
            }
            return json
        }

        fun fromWalletItem(item:WalletItem): JSONObject {
            val json = JSONObject()
            json.put(Keys.NAME, item.name)
                json.put(Keys.ADDRESS, item.address)
                when (item) {
                    is Asset -> json.put(Keys.TYPE, Keys.ASSET)
                    is Service -> json.put(Keys.TYPE, Keys.SERVICE)
                    is Token -> json.put(Keys.TYPE, Keys.TOKEN)
                }
            return json

        }

        fun toEthereumItem(dataString: String): EthereumItem? {
            try {
                val jsonObject = JSONObject(dataString)
                var address: String? = null
                if (jsonObject.has(Keys.ADDRESS)) {
                    address = jsonObject.getString(Keys.ADDRESS)
                }
                val name: String = jsonObject.getString(Keys.NAME)
                val type: String = jsonObject.getString(Keys.TYPE)
                when (type) {
                    Keys.ASSET -> {
                        return Asset(address!!, name)
                    }
                    Keys.RECORD -> {
                        val key:String = jsonObject.getString(Keys.KEY)
                        val category = jsonObject.getInt(Keys.CATEGORY)
                        if (! RecordService.CategoryIdentifier.list.contains(category)) throw InvalidCategoryException(category)
                        return Record(key, name, category)
                    }
                    Keys.SERVICE -> {
                        return Service(address!!, name)
                    }
                    Keys.TOKEN -> {
                        return Token(address!!, name)
                    }
                }
            } catch (e: Exception) {}
            return null
        }

        fun toWalletItem(jsonString:String): WalletItem? {
            val item = toEthereumItem(jsonString)
            if (item is WalletItem) {
                return item
            }
            return null
        }
    }
    class InvalidCategoryException(val givenId:Int): Exception()
    class InvalidJsonException(val jsonString:String): Exception()
}