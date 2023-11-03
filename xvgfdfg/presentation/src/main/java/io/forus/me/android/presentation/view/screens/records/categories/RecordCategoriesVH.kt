package io.forus.me.android.presentation.view.screens.records.categories

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate


class RecordCategoriesVH(parent: ViewGroup, private val clickListener: ((RecordCategory) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_record_categories)) {

   private var name: TextView? = null
    private var selectedCheckBox: CheckBox? = null
    private var root: View? = null

    init {
        name = itemView.findViewById(R.id.name)
        selectedCheckBox = itemView.findViewById(R.id.selectedCheckBox)
        root = itemView.findViewById(R.id.root)
    }

    fun render(item:  RecordCategory) = with(itemView) {


        name?.text = item.name

        root?.setOnClickListener {
            clickListener?.invoke(item)
        }

        selectedCheckBox?.setChecked(item.selected)
    }


}