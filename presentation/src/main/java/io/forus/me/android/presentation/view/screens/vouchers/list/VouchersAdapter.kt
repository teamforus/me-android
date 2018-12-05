package io.forus.me.android.presentation.view.screens.vouchers.list

import android.support.v4.view.ViewCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R.id.name
import io.forus.me.android.presentation.R.id.value
import io.forus.me.android.presentation.R.id.voucher_card
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.presentation.view.screens.vouchers.item.VoucherFragment
import kotlinx.android.synthetic.main.fragment_voucher.*
import kotlinx.android.synthetic.main.item_vouchers_list.view.*

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

    var clickListener: ((voucher: Voucher, sharedViews: List<View>, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VouchersVH(parent).apply {
        itemView.root.setOnClickListener {
            vouchers.getOrNull(adapterPosition)?.let {voucher ->

//                ViewCompat.setTransitionName(itemView.root,
//                        "card_transition_name_$adapterPosition")
//                ViewCompat.setTransitionName(itemView.name,
//                        "name_transition_name_$adapterPosition")
//                ViewCompat.setTransitionName(itemView.value,
//                        "value_transition_name_$adapterPosition")

                clickListener?.invoke(voucher, listOf(), adapterPosition)
            }
        }
    }
    override fun onBindViewHolder(holder: VouchersVH, position: Int) {
        holder.render(vouchers[position])
    }
    override fun getItemCount() = vouchers.size
    override fun getItemId(position: Int) = position.toLong()
}