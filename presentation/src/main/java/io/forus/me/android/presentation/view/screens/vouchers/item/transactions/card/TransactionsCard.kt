package io.forus.me.android.presentation.view.screens.vouchers.item.transactions.card

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import io.forus.me.android.domain.models.vouchers.Transaction

import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.vouchers.item.transactions.items.TransactionsAdapter
import kotlinx.android.synthetic.main.vouchers_transactions_view_card.view.*


class TransactionsCard : FrameLayout {
    private var mRootView: View? = null
    private lateinit var adapter: TransactionsAdapter;


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(context, attrs)
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttr(context, attrs)
        init(context)
    }

    internal fun initAttr(context: Context, attrs: AttributeSet) {


    }


    private fun init(context: Context) {

        val inflater = LayoutInflater.from(context)
        mRootView = inflater.inflate(R.layout.vouchers_transactions_view_card, this)

        adapter = TransactionsAdapter()

        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

    }

    fun setTransactions (transactions: List<Transaction>){
        adapter.transactions = transactions
        title.text = resources.getText(if(transactions.isEmpty()) R.string.transactieoverzicht_empty else R.string.transactieoverzicht)
    }
}
