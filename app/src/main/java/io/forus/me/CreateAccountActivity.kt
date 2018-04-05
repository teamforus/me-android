package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.Web3Service
import java.util.concurrent.Callable

class CreateAccountActivity : AppCompatActivity() {

    fun createAccount(view: View) {
        this.setContentView(R.layout.loading)
        ThreadHelper.await<String>(Callable {
            Web3Service.newAccount()
        })
        val intent = Intent()
        setResult(MainActivity.ResultCode.OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
    }

    fun startRecover(view: View) {

    }

}
