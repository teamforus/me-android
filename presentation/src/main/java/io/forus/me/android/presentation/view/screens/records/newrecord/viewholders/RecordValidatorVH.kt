package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.records.Validator
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.select_validator_list_item.view.*

class RecordValidatorVH(parent: ViewGroup, private val clickListener: ((Validator, Int) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.select_validator_list_item)) {
    init {

    }

    fun render(item: Validator, lastSelectedPosition: Int) = with(itemView) {

        root.setBackgroundResource(if (lastSelectedPosition == adapterPosition) R.color.alto else R.color.transparent)
        tv_name.text = item.name
        tv_title.text = item.title
        root.setOnClickListener {
            clickListener?.invoke(item, adapterPosition)
        }

    }
}