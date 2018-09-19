package io.forus.me.android.presentation.view.screens.records.categories

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_record_categories.view.*


class RecordCategoriesVH(parent: ViewGroup, private val clickListener: ((RecordCategory) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_record_categories)) {
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