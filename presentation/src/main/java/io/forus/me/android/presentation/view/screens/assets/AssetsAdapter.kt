package io.forus.me.android.presentation.view.screens.assets

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.assets.Asset

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AssetsVH(parent)
    override fun onBindViewHolder(holder: AssetsVH, position: Int) {

        holder.render(assets[position])
    }
    override fun getItemCount() = assets.size
    override fun getItemId(position: Int) = position.toLong()
}