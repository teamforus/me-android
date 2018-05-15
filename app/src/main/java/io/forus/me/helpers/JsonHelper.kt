package io.forus.me.helpers

import android.util.Log
import io.forus.me.entities.*
import io.forus.me.entities.base.EthereumItem
import io.forus.me.entities.base.WalletItem
import org.json.JSONObject

/**
 * Created by martijn.doornik on 06/03/2018.
 */
class JsonHelper {
    class Keys {
        companion object {
            const val ADDRESS: String = "address"
            const val ASSET: String = "asset"
            const val CATEGORY: String = "category"
            const val ETHER: String = "ether"
            const val NAME: String = "name"
            const val KEY: String = "key"
            const val RECORD: String = "record"
            const val TOKEN: String = "token"
            const val TYPE: String = "type"
            const val VOUCHER: String = "voucher"
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
                    json.put(Keys.NAME, item.name)
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
                is Token -> json.put(Keys.TYPE, if (!item.isEther) Keys.TOKEN else Keys.ETHER)
                is Voucher -> json.put(Keys.TYPE, Keys.VOUCHER)
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
                val name: String =
                        if (jsonObject.has(Keys.NAME)) {
                        jsonObject.getString(Keys.NAME) }
                        else ""
                val type: String = jsonObject.getString(Keys.TYPE)
                when (type) {
                    Keys.ASSET -> {
                        return Asset(address!!, name)
                    }
                    Keys.ETHER -> {
                        return Token(Token.ETHER_ADDRESS, if (name.isEmpty()) "Ether" else name)
                    }
                    Keys.RECORD -> {
                        val key:String = jsonObject.getString(Keys.KEY)
                        /*val category = jsonObject.getInt(Keys.CATEGORY)
                        return if (RecordService.CategoryIdentifier.list.contains(category))
                            Record(key, name, category)
                        else*/
                        return Record(key, name)
                    }
                    Keys.VOUCHER -> {
                        return Voucher(address!!, name)
                    }
                    Keys.TOKEN -> {
                        return Token(address!!, name)
                    }
                }
            } catch (e: Exception) {
                Log.e("JsonHelper", e.localizedMessage)
            }
            return null
        }

        fun toRecord(jsonString: String): Record? {
            val item = toEthereumItem(jsonString)
            if (item is Record) {
                return item
            }
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