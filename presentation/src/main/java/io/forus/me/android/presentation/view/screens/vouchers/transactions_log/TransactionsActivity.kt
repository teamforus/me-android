package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityTransactionsLogBinding
import io.forus.me.android.presentation.helpers.PaginationScrollListener
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.fragment.QrFragment
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherActivity
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter.TransactionsLogAdapter
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details.TransactionDetailsFragment
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels.TransactionsLogViewModel
import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*
import kotlinx.android.synthetic.main.activity_transactions_log.*
import kotlinx.android.synthetic.main.toolbar_view_records.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class TransactionsActivity : SlidingPanelActivity() {

    val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())

    companion object {

        //val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
        //val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, TransactionsActivity::class.java)
            /* if(recordCategory!=null) {
                 intent.putExtra(CATEGORY_ID_EXTRA, recordCategory.id)
                 intent.putExtra(CATEGORY_NAME_EXTRA, recordCategory.name)
             }*/
            return intent
        }
    }

    /*companion object {
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
    }*/

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

        val titleStr = "  Date"//getString(R.string.mdtp_date)
        val dateStr = "  "+dateFormat.format(item.createdAt)
        val spannable = SpannableString(titleStr+"\n"+dateStr)
        spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(TransactionsActivity@this, R.color.body_1_38)),
                0, titleStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(
                RelativeSizeSpan(0.6f),
                0, titleStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        addPopupFragment(TransactionDetailsFragment.newIntent(item.id, item.organization?.name, item.product?.organization?.name,
                NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                        .format(item.amount), item.state), spannable)


    }
}


/*: SlidingPanelActivity(), DatePickerDialog.OnDateSetListener {


companion object {

    //val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
    //val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

    fun getCallingIntent(context: Context): Intent {
        val intent = Intent(context, TransactionsActivity::class.java)
       /* if(recordCategory!=null) {
            intent.putExtra(CATEGORY_ID_EXTRA, recordCategory.id)
            intent.putExtra(CATEGORY_NAME_EXTRA, recordCategory.name)
        }*/
        return intent
    }
}

var canWork = true
private var currentPage = 1
private var isLastPage = false

private var isLoading = false
var itemCount = 0

var mustReplaceTransactionsList = false


lateinit var mainViewModel: TransactionsLogViewModel

lateinit var binding: ActivityTransactionsLogBinding


var transactionsAdapter: TransactionsLogAdapter? = null


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    mainViewModel = ViewModelProviders.of(this).get(TransactionsLogViewModel::class.java)


    binding = DataBindingUtil.setContentView<ActivityTransactionsLogBinding>(this@TransactionsActivity, R.layout.activity_transactions_log)
    binding.lifecycleOwner = this
    binding.model = mainViewModel




    info_button.visibility = View.INVISIBLE
    profile_button.setImageResource(R.drawable.ic_back)
    toolbar_title.text = "Transactions"
    profile_button.setOnClickListener { finish() }

    transactionsAdapter = TransactionsLogAdapter(arrayListOf(), object : TransactionsLogAdapter.Callback {
        override fun onItemClicked(item: Transaction) {
            try {
                showPopupQRFragment("dfg","dfgd","dfgdf","dfgdf")

            }catch (e:Exception){
                Log.d("forus","Exp = "+e.toString())
            }
        }
    })



    val llm = LinearLayoutManager(this)
    llm.orientation = LinearLayoutManager.VERTICAL
    recycler.layoutManager = llm
    recycler.adapter = transactionsAdapter

    mainViewModel.transactionsLiveData.observe(this, Observer {
        if (it != null) {
            canWork = true
            if(mustReplaceTransactionsList) transactionsAdapter!!.clearAll()
            transactionsAdapter!!.addAll(it)
            if(it.size == 0){
                this@TransactionsActivity.currentPage = 1
            }
        }
    })


    buttonDatePicker.setOnClickListener {
        val now: Calendar = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
                this,
                mainViewModel.calendarFrom.value!!.get(Calendar.YEAR),
                mainViewModel.calendarFrom.value!!.get(Calendar.MONTH),
                mainViewModel.calendarFrom.value!!.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(fragmentManager, "Datepickerdialog")

    }

    val linearLayoutManager = LinearLayoutManager(this@TransactionsActivity, LinearLayoutManager.VERTICAL, false)
    recycler.layoutManager = linearLayoutManager
    recycler.adapter = transactionsAdapter

    recycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
        override fun isLastPage(): Boolean {
            return this@TransactionsActivity.isLastPage
        }

        override fun loadMoreItems() {
            Log.d("forus","=================loadMoreItems")
            this@TransactionsActivity.isLoading = true
            mustReplaceTransactionsList = false

            mainViewModel.getTransactions( this@TransactionsActivity.currentPage)
            this@TransactionsActivity.currentPage += 1
        }

        override fun isLoading(): Boolean {
            return this@TransactionsActivity.isLoading
        }
    })

    mainViewModel.getTransactions(this@TransactionsActivity.currentPage)
    this@TransactionsActivity.currentPage += 1


}


fun showPopupQRFragment(address: String,qrHead: String? = null, qrSubtitle: String? = null, qrDescription: String? = null) {
    addPopupFragment(TransactionDetailsFragment.newIntent(address, qrHead, qrSubtitle, qrDescription), "QR code")
}



override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
    val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
    calendar.get(Calendar.DATE)
    mainViewModel.setCalendar(calendar)
    mustReplaceTransactionsList = true
    currentPage = 1
    mainViewModel.getTransactions( this@TransactionsActivity.currentPage)
    this@TransactionsActivity.currentPage += 1

}

}

*/

