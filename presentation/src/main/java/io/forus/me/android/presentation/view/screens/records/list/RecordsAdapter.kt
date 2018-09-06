package io.forus.me.android.presentation.view.screens.records.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.Record

class RecordsAdapter : RecyclerView.Adapter<RecordsVH>() {


    var records: List<Record> = emptyList()
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

    var clickListener: ((Record) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecordsVH(parent, clickListener)
    override fun onBindViewHolder(holder: RecordsVH, position: Int) {

        holder.render(records[position])
    }
    override fun getItemCount() = records.size
    override fun getItemId(position: Int) = position.toLong()
}