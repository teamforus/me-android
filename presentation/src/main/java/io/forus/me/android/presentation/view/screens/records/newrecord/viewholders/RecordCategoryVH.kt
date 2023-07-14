package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_record_select_category.view.*

class RecordCategoryVH(parent: ViewGroup, private val clickListener: ((RecordCategory, Int) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_record_select_category)) {
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