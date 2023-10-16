package io.forus.me.android.presentation.view.screens.vouchers.provider.categories

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.databinding.ItemProductCategoryBinding
import io.forus.me.android.presentation.databinding.ItemRecordSelectValidatorBinding
import io.forus.me.android.presentation.models.vouchers.ProductCategory
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.RecordValidatorVH

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

   /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryVH(parent)

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.render(items[position])
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
}