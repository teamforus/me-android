package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityTransactionsLogBinding
import io.forus.me.android.presentation.helpers.PaginationScrollListener
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter.TransactionsLogAdapter
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels.TransactionsLogViewModel
import kotlinx.android.synthetic.main.activity_transactions_log.*
import kotlinx.android.synthetic.main.toolbar_view_records.*
import java.text.SimpleDateFormat
import java.util.*


class TransactionsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {


    companion object {

        //val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
        //val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, TransactionsActivity::class.java)
            /*if(recordCategory!=null) {
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

            }
        })

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = llm
        recycler.adapter = transactionsAdapter

        mainViewModel.transactionsLiveData.observe(this, Observer {
            if (it != null) {
                canWork = true
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
                this@TransactionsActivity.isLoading = true


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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
        calendar.get(Calendar.DATE)
        mainViewModel.setCalendar(calendar)
        mainViewModel.getTransactions( this@TransactionsActivity.currentPage)
        this@TransactionsActivity.currentPage += 1

    }


}
