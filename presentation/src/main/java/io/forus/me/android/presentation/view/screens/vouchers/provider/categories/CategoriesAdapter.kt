package io.forus.me.android.presentation.view.screens.vouchers.provider.categories

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.models.vouchers.ProductCategory

class CategoriesAdapter : RecyclerView.Adapter<CategoryVH>() {

    private var lastSelectedPosition: Int = 0

    var items: List<ProductCategory> = emptyList()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryVH(parent)

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.render(items[position])
    }
    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}