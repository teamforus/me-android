package io.forus.me.android.presentation.view.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

class RVListAdapter<Item, VH: RVViewHolder<Item>>(
        private val createVH: (ViewGroup) -> VH,
        private val clickListener: ((Item) -> Unit)?
) : RecyclerView.Adapter<VH>() {

    var items: List<Item> = emptyList()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createVH(parent).also {
        it.clickListener = clickListener
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.render(items[position])
    }
    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}