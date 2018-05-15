package io.forus.me.external

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import io.forus.me.R
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.services.IdentityService

/**
 * Created by martijn.doornik on 07/03/2018.
 */
class LoginActivity : Activity() {
    private var checkBox: CheckBox? = null
    private lateinit var list: RecyclerView

    fun onAccept(view: View) {
        if (userAgrees) {
            val result = Intent("LOGIN")
            result.putExtra("success", true)
            result.putExtra("address", IdentityService.currentAddress)
            setResult(RESULT_OK, result)
            finish()
        } else {
            this.checkBox!!.setTextColor(Color.RED)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.external_activity_login)
        println(callingActivity.className)
        val raw = intent.extras["attributes"]
        if (raw is Array<*>) {
            val items: List<EthereumItem> = MutableList<EthereumItem>(raw.size, {index: Int ->
                // TODO fix hardcoding
                Token("TestAddress", raw[index].toString(), "0x01234567890123456789", 123)
            })
            list = findViewById(R.id.external_login_attribute_container)
            list.layoutManager = LinearLayoutManager(this)
            list.adapter = EthereumItemList(items)
        }
    }

    fun onDecline(view: View) {
        val result = Intent("CANCEL")
        result.putExtra("success", false)
        setResult(RESULT_CANCELED, result)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    val userAgrees: Boolean
        get() {
            if (this.checkBox == null) {
                checkBox = findViewById(R.id.external_login_accept_checkbox)
            }
            return (checkBox!!.isChecked)
        }
}