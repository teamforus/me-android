package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityTransactionsLogBinding
import io.forus.me.android.presentation.helpers.PaginationScrollListener
import io.forus.me.android.presentation.view.screens.account.assigndelegates.qr.RestoreByQRFragment
import io.forus.me.android.presentation.view.screens.dashboard.DashboardActivity
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter.TransactionsLogAdapter
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.details.TransactionDetailsFragment
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels.TransactionsLogViewModel
import kotlinx.android.synthetic.main.activity_transactions_log.*
import kotlinx.android.synthetic.main.fragment_voucher.*
import kotlinx.android.synthetic.main.toolbar_view_records.*
import java.util.*

class TransactionsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val GRP_ID = "GRP_ID"

        @JvmStatic
        fun newInstance() =
                TransactionsFragment().apply {

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

    }


    override fun onCreateView(inflater: LayoutInflater,
                              @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {


        val viewRoot = LayoutInflater.from(context!!).inflate(R.layout.activity_transactions_log, null)
        binding = ActivityTransactionsLogBinding.bind(viewRoot)
        val view = binding.root

        return view
    }


//var circlesOptions: CircleOptions? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel = ViewModelProviders.of(this).get(TransactionsLogViewModel::class.java)


        binding.lifecycleOwner = this
        binding.model = mainViewModel


        info_button.visibility = View.INVISIBLE
        profile_button.setImageResource(R.drawable.ic_back)
        toolbar_title.text = "Transactions"
        profile_button.setOnClickListener { activity!!.finish() }

        transactionsAdapter = TransactionsLogAdapter(arrayListOf(), object : TransactionsLogAdapter.Callback {
            override fun onItemClicked(item: Transaction) {
                try {
                 //  showPopupQRFragment("dfg","dfgd","dfgdf","dfgdf")

                    (activity as? TransactionsActivity)?.showPopupTransactionDetailsFragment(item)

                } catch (e: Exception) {
                    Log.d("forus", "Exp = " + e.toString())
                }
            }
        })


        val llm = LinearLayoutManager(context!!)
        llm.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = llm
        recycler.adapter = transactionsAdapter

        mainViewModel.transactionsLiveData.observe(this, Observer {
            if (it != null) {
                canWork = true
                if (mustReplaceTransactionsList) transactionsAdapter!!.clearAll()
                transactionsAdapter!!.addAll(it)
                if (it.size == 0) {
                    this@TransactionsFragment.currentPage = 1
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
            dpd.show(activity!!.fragmentManager, "Datepickerdialog")

        }

        val linearLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = transactionsAdapter

        recycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return this@TransactionsFragment.isLastPage
            }

            override fun loadMoreItems() {
                Log.d("forus", "=================loadMoreItems")
                this@TransactionsFragment.isLoading = true
                mustReplaceTransactionsList = false

                mainViewModel.getTransactions(this@TransactionsFragment.currentPage)
                this@TransactionsFragment.currentPage += 1
            }

            override fun isLoading(): Boolean {
                return this@TransactionsFragment.isLoading
            }
        })

        mainViewModel.getTransactions(this@TransactionsFragment.currentPage)
        this@TransactionsFragment.currentPage += 1

    }


    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
        calendar.get(Calendar.DATE)
        mainViewModel.setCalendar(calendar)
        mustReplaceTransactionsList = true
        currentPage = 1
        mainViewModel.getTransactions(this@TransactionsFragment.currentPage)
        this@TransactionsFragment.currentPage += 1

    }

    fun blurBackground() {

        /*name.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        type.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        value.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        value.paint.maskFilter = BlurMaskFilter(value.textSize / 4, BlurMaskFilter.Blur.NORMAL)
        name.paint.maskFilter = BlurMaskFilter(name.textSize / 4, BlurMaskFilter.Blur.NORMAL)
        type.paint.maskFilter = BlurMaskFilter(type.textSize / 4, BlurMaskFilter.Blur.NORMAL)*/
    }

    fun unblurBackground() {

       /* name.setLayerType(View.LAYER_TYPE_NONE, null)
        type.setLayerType(View.LAYER_TYPE_NONE, null)
        value.setLayerType(View.LAYER_TYPE_NONE, null)

        value.paint.maskFilter = null
        name.paint.maskFilter = null
        type.paint.maskFilter = null*/
    }


}
