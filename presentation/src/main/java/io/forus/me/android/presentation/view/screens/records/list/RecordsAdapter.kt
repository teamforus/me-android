package io.forus.me.android.presentation.view.screens.records.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.databinding.ItemRecordsBinding

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsVH {
        val binding = ItemRecordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordsVH(binding)
    }

    override fun onBindViewHolder(holder: RecordsVH, position: Int) {
        val item = records[position]
        holder.bind(item ){

        }
    }

    override fun getItemCount() = records.size
    override fun getItemId(position: Int) = position.toLong()
}