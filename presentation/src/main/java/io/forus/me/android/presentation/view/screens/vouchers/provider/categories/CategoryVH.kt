package io.forus.me.android.presentation.view.screens.vouchers.provider.categories

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.presentation.databinding.ItemProductCategoryBinding
import io.forus.me.android.presentation.models.vouchers.ProductCategory


class CategoryVH(private val binding: ItemProductCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: ProductCategory,
    ) {
        binding.apply {
            tvName.text = item.name
        }
    }
}