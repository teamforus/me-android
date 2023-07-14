package io.forus.me.android.presentation.view.screens.records.categories

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.domain.models.records.RecordCategory

class RecordCategoriesAdapter : RecyclerView.Adapter<RecordCategoriesVH>() {


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

    var clickListener: ((RecordCategory) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecordCategoriesVH(parent, clickListener)
    override fun onBindViewHolder(holder: RecordCategoriesVH, position: Int) {

        holder.render(items[position])
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()


    fun selectItem(item: RecordCategory) {

        items.forEach {
            it.selected = it.id == item.id
        }
        notifyDataSetChanged()
    }
}