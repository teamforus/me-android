package io.forus.me.android.presentation.view.screens.records.categories

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.CompoundButton
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_record_categories.view.*


class RecordCategoriesVH(parent: ViewGroup, private val clickListener: ((RecordCategory) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_record_categories)) {
    init {

    }

    fun render(item:  RecordCategory) = with(itemView) {


        name.text = item.name

        root.setOnClickListener {
            clickListener?.invoke(item)
        }

        selectedCheckBox.setChecked(item.selected)
    }


}