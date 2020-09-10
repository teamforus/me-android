package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityActionsBinding
import io.forus.me.android.presentation.databinding.ActivityTransactionsLogBinding
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter.TransactionsLogAdapter
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels.TransactionsLogViewModel
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.ActionsActivity
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.model.ActionsViewModel
import kotlinx.android.synthetic.main.activity_transactions_log.*
import kotlinx.android.synthetic.main.toolbar_view_records.*

class TransactionsActivity : AppCompatActivity() {


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
            override fun onItemClicked(item: Foo) {

            }
        })

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = llm
        recycler.adapter = transactionsAdapter

        mainViewModel.transactionsLiveData.observe(this, Observer {
            transactionsAdapter!!.setList(it!!)
        })

        mainViewModel.fetchList()


    }
}
