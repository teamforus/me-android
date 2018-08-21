package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.select_category_list_item.view.*

class RecordCategoryVH(parent: ViewGroup, private val clickListener: ((RecordCategory, Int) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.select_category_list_item)) {
    init {

    }

    fun render(item:  RecordCategory, lastSelectedPosition: Int) = with(itemView) {

        bg.setBackgroundResource(if (lastSelectedPosition == adapterPosition) R.color.rippleColor else R.color.alabaster)
        tv_name.text = item.name
        if(item.logo.isNotEmpty()) iv_logo.setImageUrl(item.logo)
        root.setOnClickListener {
            clickListener?.invoke(item, adapterPosition)
        }

    }
}