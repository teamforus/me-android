package io.forus.me.android.presentation.view.screens.vouchers.provider.categories

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.presentation.models.vouchers.ProductCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_product_category.view.*

class CategoryVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_product_category)) {
    init {

    }

    fun render(item: ProductCategory) = with(itemView) {
        tv_name.text = item.name
    }
}