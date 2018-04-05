package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.forus.me.helpers.QrHelper
import io.forus.me.services.Web3Service

import kotlinx.android.synthetic.main.activity_assign_delegates.*
import org.json.JSONObject

class AssignDelegatesActivity : AppCompatActivity() {

    fun next(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_delegates)
        setSupportActionBar(toolbar)

        val json = JSONObject()
        json.put("address", Web3Service.account!!)
        json.put("type", "request_delegate")

        qr_view.setImageBitmap(QrHelper.getQrBitmap(json))


    }

}
