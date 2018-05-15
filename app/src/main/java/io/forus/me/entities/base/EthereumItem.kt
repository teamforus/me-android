package io.forus.me.entities.base

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.EncodeHintType
import io.forus.me.helpers.JsonHelper
import io.forus.me.helpers.QrHelper
import net.glxn.qrgen.android.QRCode
import org.json.JSONObject

/**
 * Created by martijn.doornik on 23/02/2018.
 */
@Entity
abstract class EthereumItem(
        @ColumnInfo(name="address")
        var address: String,
        var name: String,
        var identity: String) {

    @PrimaryKey
    var id: Long? = null
    var errorMessage: String = ""

    val hasError: Boolean
            get() = errorMessage.isNotEmpty()

    companion object {
        fun fromString(input: String): EthereumItem? {
            return JsonHelper.toEthereumItem(input)
        }
    }

    fun toJson(): JSONObject {
        return JsonHelper.fromEthereumItem(this)
    }

    /**
     * Synchronize the Ethereum item with its corresponding network.
     * @return True is changed, false if not
     */
    abstract fun sync(): Boolean
}