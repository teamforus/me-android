package io.forus.me.android.presentation.view.screens.assets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.presentation.databinding.ItemAssetsBinding

class AssetsAdapter : RecyclerView.Adapter<AssetsVH>() {


    var assets: List<Asset> = emptyList()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetsVH {
        val binding = ItemAssetsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssetsVH(binding)
    }

    override fun onBindViewHolder(holder: AssetsVH, position: Int) {
        val item = assets[position]
        holder.bind(item)
    }

    override fun getItemCount() = assets.size
    override fun getItemId(position: Int) = position.toLong()
}