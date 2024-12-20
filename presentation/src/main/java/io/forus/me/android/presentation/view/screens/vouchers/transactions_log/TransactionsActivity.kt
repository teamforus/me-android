package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

//import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details.TransactionDetailsPopupDialog
import java.text.SimpleDateFormat
import java.util.Locale


class TransactionsActivity : CommonActivity() {

    val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())

    override val viewID: Int
        get() = R.layout.activity_layout

    companion object {


        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, TransactionsActivity::class.java)

            return intent
        }
    }

    private lateinit var fragment: TransactionsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fragment = TransactionsFragment.newInstance()

            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    fun showPopupTransactionDetailsFragment(transaction: Transaction) {

        val transactionDetailsPopupDialog = TransactionDetailsPopupDialog(transaction)
        transactionDetailsPopupDialog.show(this.supportFragmentManager, "TransactionDetailsPopupDialog")

       /* val typeface =
            ResourcesCompat.getFont(this@TransactionsActivity, R.font.fgoogle_sans_regular);

        val titleStr = "   " + getString(R.string.date_title)
        val dateStr = "  " + dateFormat.format(item.createdAt)
        val totalStr = titleStr + "\n" + dateStr
        val spannable = SpannableString(totalStr)



        spannable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    TransactionsActivity@ this,
                    R.color.body_1_38
                )
            ),
            0, titleStr.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            RelativeSizeSpan(0.6f),
            0, titleStr.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            CustomTypefaceSpan("", typeface!!),
            0,
            totalStr.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        var state = ""
        when (item.state) {
            "success" -> getString(R.string.status_success)
            "pending" -> getString(R.string.status_pending)
            else -> state = item.state ?: ""
        }

        val meBottomSheet = MeBottomSheetDialogFragment.newInstance(
            TransactionDetailsFragment.newIntent(
                item.id, item.fund?.name,
                item.organization?.name,
                NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                    .format(item.amount), state
            ), spannable.toString())
        meBottomSheet.show(supportFragmentManager, meBottomSheet.tag)*/

    }
}



