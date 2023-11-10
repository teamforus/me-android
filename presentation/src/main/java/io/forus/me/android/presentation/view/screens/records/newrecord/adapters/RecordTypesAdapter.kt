package io.forus.me.android.presentation.view.screens.records.newrecord.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.databinding.ItemRecordSelectTypeBinding
import io.forus.me.android.presentation.databinding.ItemWalletsBinding
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.RecordTypeVH
import io.forus.me.android.presentation.view.screens.wallets.WalletsVH

class RecordTypesAdapter(private val clickListener: ((RecordType) -> Unit)?): RecyclerView.Adapter<RecordTypeVH>() {

    private var lastSelectedPosition: Int = -1

    var items: List<RecordType> = emptyList()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordTypeVH {
        val binding = ItemRecordSelectTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordTypeVH(binding)
    }

    override fun onBindViewHolder(holder: RecordTypeVH, position: Int) {
        val item = items[position]
        holder.bind(item, lastSelectedPosition){ recordType: RecordType, position: Int ->
            lastSelectedPosition = position
            notifyDataSetChanged()
            clickListener?.invoke(recordType)
        }
    }


    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}