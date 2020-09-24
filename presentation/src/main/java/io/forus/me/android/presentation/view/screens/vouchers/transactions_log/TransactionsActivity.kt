package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.component.CustomTypefaceSpan
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details.TransactionDetailsFragment
import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class TransactionsActivity : SlidingPanelActivity() {

    val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())

    companion object {


        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, TransactionsActivity::class.java)

            return intent
        }
    }

    override val height: Float
        get() = 448f


    private lateinit var fragment: TransactionsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (savedInstanceState == null) {
            fragment = TransactionsFragment.newInstance()

            addFragment(R.id.fragmentContainer, fragment)
        }


        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                when (newState) {
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

    fun showPopupTransactionDetailsFragment(item: Transaction){

        //val font = Typeface.createFromAsset(assets, R.font.fgoogle_sans_regular)
        val typeface = ResourcesCompat.getFont(this@TransactionsActivity, R.font.fgoogle_sans_regular);

        val titleStr = "   Date"//getString(R.string.mdtp_date)
        val dateStr = "  "+dateFormat.format(item.createdAt)
        val totalStr = titleStr+"\n"+dateStr
        val spannable = SpannableString(totalStr)



        spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(TransactionsActivity@this, R.color.body_1_38)),
                0, titleStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(
                RelativeSizeSpan(0.6f),
                0, titleStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(CustomTypefaceSpan("", typeface!!), 0, totalStr.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)


        addPopupFragment(TransactionDetailsFragment.newIntent(item.id, item.organization?.name, item.product?.organization?.name,
                NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                        .format(item.amount), item.state), spannable)


    }
}



