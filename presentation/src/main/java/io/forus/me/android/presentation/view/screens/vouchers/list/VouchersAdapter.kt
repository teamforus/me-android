package io.forus.me.android.presentation.view.screens.vouchers.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.presentation.databinding.ItemVouchersListBinding
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

    var clickListener: ((voucher: Voucher, sharedViews: List<View>, position: Int) -> Unit)? = null

   /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VouchersVH(parent).apply {
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
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VouchersVH {
        val binding = ItemVouchersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VouchersVH(binding)
    }

    override fun onBindViewHolder(holder: VouchersVH, position: Int) {
        val item = vouchers[position]
        holder.bind(item){ voucher ->
            clickListener?.invoke(voucher, listOf(),position)
        }
    }

    override fun getItemCount() = vouchers.size
    override fun getItemId(position: Int) = position.toLong()
}