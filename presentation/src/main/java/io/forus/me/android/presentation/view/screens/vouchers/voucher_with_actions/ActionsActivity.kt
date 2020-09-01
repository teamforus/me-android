package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.models.vouchers.Voucher
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityActionsBinding
import io.forus.me.android.presentation.helpers.PaginationScrollListener
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.adapter.ActionsAdapter
import io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.model.ActionsViewModel
import kotlinx.android.synthetic.main.activity_actions.*


class ActionsActivity : AppCompatActivity() {


    companion object {


        val VOUCHER_ADDRESS_EXTRA = "VOUCHER_ADDRESS_EXTRA"

        fun getCallingIntent(context: Context, voucherAddress: String): Intent {
            val intent = Intent(context, ActionsActivity::class.java)
            intent.putExtra(VOUCHER_ADDRESS_EXTRA, voucherAddress)
            return intent
        }
    }

    var voucherAddress: String? = null

    lateinit var mainViewModel: ActionsViewModel

    lateinit var binding: ActivityActionsBinding


    var transactionsAdapter: ActionsAdapter? = null



    private var currentPage = 0
    private var isLastPage = false

    private var isLoading = false
    var itemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        mainViewModel = ViewModelProviders.of(this).get(ActionsViewModel::class.java)// ViewModelProvider(this).get(TransactionsStoryViewModel::class.java)
        voucherAddress = intent.getSerializableExtra(VOUCHER_ADDRESS_EXTRA) as String
         mainViewModel.voucherAddress = voucherAddress
        binding = DataBindingUtil.setContentView<ActivityActionsBinding>(this@ActionsActivity, R.layout.activity_actions)
        binding.lifecycleOwner = this
        binding.model = mainViewModel

        transactionsAdapter = ActionsAdapter(arrayListOf(), object : ActionsAdapter.Callback {
            override fun onItemClicked(item: ProductAction) {

            }
        })

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL


        mainViewModel.productActionsLiveData.observe(this, Observer {

            if(it != null) {
                transactionsAdapter!!.addAll(it);
            }

        })

        mainViewModel.voucher.observe(this, Observer {
            if(it != null) {
                refreshVoucherUI(it)
                mainViewModel.getVoucherActionGoods(0)//fetchList(page)
                //page++
            }
        })

        mainViewModel.getVoucherDetails()




        val linearLayoutManager =  LinearLayoutManager(this@ActionsActivity , LinearLayoutManager.VERTICAL, false)
        recycler.setLayoutManager(linearLayoutManager)
        recycler.adapter = transactionsAdapter

        recycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return this@ActionsActivity.isLastPage;
            }

            override fun loadMoreItems() {
                this@ActionsActivity.isLoading = true;
                this@ActionsActivity.currentPage += 1;
                //loadNextPage();
                mainViewModel.getVoucherActionGoods(this@ActionsActivity.currentPage)
            }

            override fun isLoading(): Boolean {
                return this@ActionsActivity.isLoading;
            }
        })


    }

    fun refreshVoucherUI(voucher: Voucher){
        tv_name.text = voucher.fundName

        organizationsSpinner



        val catNames: MutableList<String> = ArrayList()
        catNames.add("Барсик")
        catNames.add("Мурзик")
        catNames.add("Рыжик")

        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, catNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)



        organizationsSpinner.setAdapter(adapter)
    }
}