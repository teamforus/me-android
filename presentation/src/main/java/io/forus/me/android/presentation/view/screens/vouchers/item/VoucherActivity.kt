package io.forus.me.android.presentation.view.screens.vouchers.item


import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.fragment.QrFragment


class VoucherActivity : SlidingPanelActivity() {

    companion object {
         val ID_EXTRA = "ID_EXTRA"

        fun getCallingIntent(context: Context, id: String): Intent {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra(ID_EXTRA, id)
            return intent
        }
    }

    private lateinit var fragment: VoucherFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fragment = VoucherFragment.newIntent(intent.getStringExtra(ID_EXTRA))
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    fun showPopupQRFragment(address: String){
        addPopupFragment(QrFragment.newIntent(address), "QR code")
    }
}
