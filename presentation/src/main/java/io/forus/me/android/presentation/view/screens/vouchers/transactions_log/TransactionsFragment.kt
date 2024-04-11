package io.forus.me.android.presentation.view.screens.vouchers.transactions_log

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityTransactionsLogBinding
import io.forus.me.android.presentation.helpers.PaginationScrollListener
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter.TransactionsLogAdapter
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.viewmodels.TransactionsLogViewModel
import java.util.*

class TransactionsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val GRP_ID = "GRP_ID"

        @JvmStatic
        fun newInstance() =
            TransactionsFragment().apply {

            }
    }

    var info_button: View? = null
    var profile_button: io.forus.me.android.presentation.view.component.images.AutoLoadImageView? =
        null
    var toolbar_title: io.forus.me.android.presentation.view.component.text.TextView? = null


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


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        binding = ActivityTransactionsLogBinding.inflate(inflater)

        profile_button = binding.root.findViewById(R.id.profile_button)
        info_button = binding.root.findViewById(R.id.info_button)
        toolbar_title = binding.root.findViewById(R.id.toolbar_title)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel = ViewModelProviders.of(this).get(TransactionsLogViewModel::class.java)


        binding.lifecycleOwner = this
        binding.model = mainViewModel


        info_button?.visibility = View.INVISIBLE
        profile_button?.setImageResource(R.drawable.ic_back)
        toolbar_title?.text = getString(R.string.transactions_title)
        profile_button?.setOnClickListener { requireActivity().finish() }

        transactionsAdapter = TransactionsLogAdapter(
            requireContext(),
            arrayListOf(),
            object : TransactionsLogAdapter.Callback {
                override fun onItemClicked(item: Transaction) {

                    (activity as? TransactionsActivity)?.showPopupTransactionDetailsFragment(item)

                }
            })


        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.recycler.layoutManager = llm
        binding.recycler.adapter = transactionsAdapter

        mainViewModel.transactionsLiveData.observe(viewLifecycleOwner, Observer {
            this@TransactionsFragment.isLoading = false
            if (it != null) {
                canWork = true
                if (mustReplaceTransactionsList) transactionsAdapter!!.clearAll()
                transactionsAdapter!!.addAll(it)
                if (it.size == 0) {
                    this@TransactionsFragment.currentPage = 1
                }
            }
        })


        binding.buttonDatePicker.setOnClickListener {
            val now: Calendar = Calendar.getInstance()
            val dpd = DatePickerDialog.newInstance(
                this,
                mainViewModel.calendarFrom.value!!.get(Calendar.YEAR),
                mainViewModel.calendarFrom.value!!.get(Calendar.MONTH),
                mainViewModel.calendarFrom.value!!.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show(requireFragmentManager(), "Datepickerdialog")

        }

        val linearLayoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        binding.recycler.layoutManager = linearLayoutManager
        binding.recycler.adapter = transactionsAdapter

        binding.recycler.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return this@TransactionsFragment.isLastPage
            }

            override fun loadMoreItems() {
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


}
