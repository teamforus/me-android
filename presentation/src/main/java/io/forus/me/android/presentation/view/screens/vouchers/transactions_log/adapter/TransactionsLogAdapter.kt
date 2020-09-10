package io.forus.me.android.presentation.view.screens.vouchers.transactions_log.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.vouchers.transactions_log.Foo

class TransactionsLogAdapter(var items: ArrayList<Foo>, val callback: Callback) : RecyclerView.Adapter<TransactionsLogAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transactions_list, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTV = itemView.findViewById<TextView>(R.id.nameTV)



        fun bind(item: Foo) {
            nameTV.text = item.name

        }
    }

    interface Callback {
        fun onItemClicked(item: Foo)
    }

    fun setList(goods: List<Foo>) {

        Log.d("my","ADAPTER_goods_size = ${goods.size}")

        items.clear();
        items.addAll(goods);
        this.notifyDataSetChanged();
    }

}
