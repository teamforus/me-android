package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.domain.models.vouchers.Transaction
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionsLogAdapter(var items: ArrayList<Transaction>, val callback: Callback) : RecyclerView.Adapter<TransactionsLogAdapter.MainHolder>() {

    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false

    val dateFormat = SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transactions_list, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val subtitle1 = itemView.findViewById<TextView>(R.id.subtitle1)
        private val subtitle2 = itemView.findViewById<TextView>(R.id.subtitle2)
        private val overline1 = itemView.findViewById<TextView>(R.id.overline1)
        private val overline2 = itemView.findViewById<TextView>(R.id.overline2)


        fun bind(item: Transaction) {
            subtitle1.text = item.product?.name
            subtitle2.text = item.amount?.toString()
            overline1.text = VoucherFragment.dateFormat.format(item.createdAt)
            overline2.text =item.state

        }
    }

    interface Callback {
        fun onItemClicked(item: Transaction)
    }

    fun setList(goods: List<Transaction>) {

        Log.d("my","ADAPTER_goods_size = ${goods.size}")

        items.clear();
        items.addAll(goods);
        this.notifyDataSetChanged();
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == items!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }


    fun clearAll() {

        items.clear()
        notifyDataSetChanged()
    }

    fun add(item: Transaction) {
        items.add(item!!)
        notifyItemInserted(items!!.size - 1)
    }

    fun addAll(moveResults: List<Transaction>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun getItem(position: Int): Transaction {
        return items!![position]
    }

}
