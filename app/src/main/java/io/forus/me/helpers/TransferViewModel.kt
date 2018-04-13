package io.forus.me.helpers

import io.forus.me.entities.base.EthereumItem
import io.forus.me.entities.base.WalletItem
import org.json.JSONObject

/**
 * Created by martijn.doornik on 11/04/2018.
 */
class TransferViewModel(val item: WalletItem, val description:String, val value: Number? = null) {

    /**
     * Converts the view into a parsable json format
     * @return The model that can be used in Intents, url's and QR-codes
     */
    fun toJson(): JSONObject {
        val ret = JSONObject()
        ret.put(Json.DESCRIPTION, this.description)
        ret.put(Json.VALUE, this.value)
        ret.put(Json.ITEM, this.item.toJson())
        return ret
    }

    companion object {

        /**
         * Converts json String to TransferViewModel. The JSON keys can be found in
         * TransferViewModel.Json subclass
         * @param jsonString The string which contains the viewmodel
         * @return The model as presented in the jsonString

         */
        fun fromJson(jsonString: String): TransferViewModel? {
            var ret: TransferViewModel? = null
            try {
                val json = JSONObject(jsonString)
                val item = JsonHelper.toWalletItem(json.getString(Json.ITEM))
                if (item != null) {
                    var value: Number? = null
                    if (json.has(Json.VALUE)) value = json.getDouble(Json.VALUE)
                    val description = json.getString(Json.DESCRIPTION)
                    ret = TransferViewModel(item, description, value)
                }
            } catch (e:Throwable) {}
            return ret
        }
    }

    class Json {
        companion object {
            val DESCRIPTION = "description"
            val ITEM = "item"
            val VALUE = "value"
        }
    }
}