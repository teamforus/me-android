package io.forus.me.android.presentation.view.screens.vouchers.item.transactions

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.presentation.databinding.ItemVoucherTranscationsListBinding
import io.forus.me.android.presentation.models.vouchers.Transaction

class TransactionsAdapter : RecyclerView.Adapter<TransactionsVH>() {


    var transactions: List<Transaction> = emptyList()
        set(value) {
            Log.d("my","set value transactions ${transactions.size}")
            val old = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = old.size
                override fun getNewListSize() = field.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition] == field[newItemPosition]

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition] == field[newItemPosition]
            }).dispatchUpdatesTo(this)
            notifyDataSetChanged()
        }

    var isActionsVoucher = false

    init {
        setHasStableIds(true)
    }

    var clickListener: ((Transaction) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsVH {
        val binding = ItemVoucherTranscationsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionsVH(binding)
    }

    override fun onBindViewHolder(holder: TransactionsVH, position: Int) {
        val item = transactions[position]
        holder.bind(item) { transaction: Transaction ->
            clickListener?.invoke(transaction)
        }
    }

    override fun getItemCount() = transactions.size
    override fun getItemId(position: Int) = position.toLong()
}