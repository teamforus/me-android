package io.forus.me.android.presentation.view.screens.records.newrecord.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.databinding.ItemRecordSelectCategoryBinding
import io.forus.me.android.presentation.databinding.ItemWalletsBinding
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.RecordCategoryVH
import io.forus.me.android.presentation.view.screens.wallets.WalletsVH

class RecordCategoriesAdapter(private val clickListener: ((RecordCategory) -> Unit)?): RecyclerView.Adapter<RecordCategoryVH>() {

    private var lastSelectedPosition: Int = -1

    var items: List<RecordCategory> = emptyList()
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordCategoryVH {
        val binding = ItemRecordSelectCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordCategoryVH(binding)
    }

    override fun onBindViewHolder(holder: RecordCategoryVH, position: Int) {
        val item = items[position]
        holder.bind(item,lastSelectedPosition){ recordCategory: RecordCategory, position: Int ->
            lastSelectedPosition = position
            //notifyDataSetChanged()
            clickListener?.invoke(recordCategory)
        }
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}