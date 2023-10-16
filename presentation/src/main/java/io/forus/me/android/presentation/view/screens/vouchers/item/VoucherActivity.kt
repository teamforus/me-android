package io.forus.me.android.presentation.view.screens.vouchers.item


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.base.MViewModelProvider
import io.forus.me.android.presentation.view.screens.vouchers.VoucherViewModel


class VoucherActivity : SlidingPanelActivity(), MViewModelProvider<VoucherViewModel> {

    override val viewModel: VoucherViewModel by viewModels()

    companion object {
         const val ID_EXTRA = "ID_EXTRA"
         const val VOUCHER_EXTRA = "VOUCHER_EXTRA"

        fun getCallingIntent(context: Context, id: String): Intent {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra(ID_EXTRA, id)
            return intent
        }

        fun getCallingIntent(context: Context, voucher: Voucher): Intent {
            val intent = Intent(context, VoucherActivity::class.java)
            intent.putExtra(VOUCHER_EXTRA, voucher)
            return intent
        }
    }

    private lateinit var fragment: VoucherFragment

    private var slidingLayout:com.sothree.slidinguppanel.SlidingUpPanelLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val voucher = intent.getParcelableExtra<Voucher>(VOUCHER_EXTRA)

        viewModel.setVoucher(voucher)
        viewModel.setAddress(voucher?.address?:"")

        if (savedInstanceState == null) {
            fragment = when (voucher) {
                null -> VoucherFragment.newInstance(intent.getStringExtra(ID_EXTRA)?:"")
                else -> VoucherFragment.newInstance(voucher)
            }

            addFragment(R.id.fragmentContainer, fragment)
        }

        slidingLayout = findViewById(R.id.sliding_layout)

        slidingLayout?.addPanelSlideListener(object: SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                when(newState){
                    SlidingUpPanelLayout.PanelState.EXPANDED, SlidingUpPanelLayout.PanelState.ANCHORED -> {
                        fragment.blurBackground()
                    }
                    else -> {
                        fragment.unblurBackground()
                    }
                }
            }
        })
    }

    /*fun showPopupQRFragment(address: String){
        addPopupFragment(QrFragment.newIntent(address, resources.getString(R.string.voucher_qr_code_subtitle), resources.getString(R.string.voucher_qr_code_description)), "QR code")
    }*/
}
