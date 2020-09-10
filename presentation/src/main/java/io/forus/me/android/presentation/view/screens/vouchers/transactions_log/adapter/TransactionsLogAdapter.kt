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

class TransactionsLogAdapter(var items: ArrayList<Transaction>, val callback: Callback) : RecyclerView.Adapter<TransactionsLogAdapter.MainHolder>() {

    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transactions_list, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTV = itemView.findViewById<TextView>(R.id.nameTV)



        fun bind(item: Transaction) {
            nameTV.text = item.product?.name

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
