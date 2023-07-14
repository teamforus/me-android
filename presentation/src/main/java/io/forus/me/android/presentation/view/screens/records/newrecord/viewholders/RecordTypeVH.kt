package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_record_select_type.view.*

class RecordTypeVH(parent: ViewGroup, private val clickListener: ((RecordType, Int) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_record_select_type)) {
    init {

    }

    fun render(item:  RecordType, lastSelectedPosition: Int) = with(itemView) {

        selectedCheckBox.setChecked (lastSelectedPosition == adapterPosition)
        name.text = item.name
        root.setOnClickListener {
            clickListener?.invoke(item, adapterPosition)
        }

    }
}