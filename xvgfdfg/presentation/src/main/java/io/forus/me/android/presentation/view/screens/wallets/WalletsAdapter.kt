package io.forus.me.android.presentation.view.screens.wallets

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.wallets.Wallet
import io.forus.me.android.presentation.databinding.ItemWalletsBinding

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletsVH {
        val binding = ItemWalletsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletsVH(binding)
    }

    override fun onBindViewHolder(holder: WalletsVH, position: Int) {
        val item = wallets[position]
        holder.bind(item, clickListener)
    }


    override fun getItemCount() = wallets.size
    override fun getItemId(position: Int) = position.toLong()
}