package io.forus.me.android.presentation.view.screens.vouchers.provider.categories

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.vouchers.ProductCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.select_organization_list_item.view.*

class CategoryVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.product_category_list_item)) {
    init {

    }

    fun render(item: ProductCategory) = with(itemView) {
        tv_name.text = item.name
    }
}