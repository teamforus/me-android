package io.forus.me.android.presentation.view.screens.privacy_and_security

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.vouchers.product_reservation.ProductReservationActivity

class PrivacyAndSecurityActivity : AppCompatActivity() {

    companion object {

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, PrivacyAndSecurityActivity::class.java)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_and_security)

        val rootFragment = PrivacyAndSecurityFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.root_container, rootFragment)
                .commitAllowingStateLoss()
    }

    fun getFragmentContainerId() = R.id.root_container


}