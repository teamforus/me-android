package io.forus.me.android.presentation.view.screens.records.categories

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.records_categories_list_item.view.*


class RecordCategoriesVH(parent: ViewGroup, private val clickListener: ((RecordCategory) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.records_categories_list_item)) {
    init {

    }

    fun render(item:  RecordCategory) = with(itemView) {

        num_records.text = "${item.size} eigenschappen"
        name.text = item.name

        if(item.logo.isNotEmpty()) logo.setImageUrl(item.logo)

        root.setOnClickListener {
            clickListener?.invoke(item)
        }

    }
}