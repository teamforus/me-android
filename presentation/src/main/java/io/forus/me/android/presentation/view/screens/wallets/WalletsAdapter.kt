package io.forus.me.android.presentation.view.screens.wallets

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.wallets.Wallet

class WalletsAdapter : RecyclerView.Adapter<WalletsVH>() {


    var wallets: List<Wallet> = emptyList()
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

    var clickListener: ((Wallet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WalletsVH(parent, clickListener)
    override fun onBindViewHolder(holder: WalletsVH, position: Int) {

        holder.render(wallets[position])
    }
    override fun getItemCount() = wallets.size
    override fun getItemId(position: Int) = position.toLong()
}