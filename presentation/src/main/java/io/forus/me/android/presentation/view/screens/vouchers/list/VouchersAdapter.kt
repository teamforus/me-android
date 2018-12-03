package io.forus.me.android.presentation.view.screens.vouchers.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.models.vouchers.Voucher

class VouchersAdapter : RecyclerView.Adapter<VouchersVH>() {


    var vouchers: List<Voucher> = emptyList()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = old.size
                override fun getNewListSize() = field.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == field[newItemPosition]
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == field[newItemPosition]
            }).dispatchUpdatesTo(this)
            notifyDataSetChanged()
        }

    
    init {
        setHasStableIds(true)
    }

    var clickListener: ((Voucher) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VouchersVH(parent).apply {
        itemView.root.setOnClickListener {
            vouchers.getOrNull(adapterPosition)?.let {voucher ->
                clickListener?.invoke(voucher)
            }
        }
    }
    override fun onBindViewHolder(holder: VouchersVH, position: Int) {
        holder.render(vouchers[position])
    }
    override fun getItemCount() = vouchers.size
    override fun getItemId(position: Int) = position.toLong()
}