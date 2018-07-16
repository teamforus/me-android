package io.forus.me.android.presentation.view.screens.records.newrecord.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.RecordCategoryVH

class RecordCategoriesAdapter(private val clickListener: ((RecordCategory) -> Unit)?): RecyclerView.Adapter<RecordCategoryVH>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecordCategoryVH(parent, clickListener)
    override fun onBindViewHolder(holder: RecordCategoryVH, position: Int) {

        holder.render(items[position])
    }
    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}