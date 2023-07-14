package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_record_select_validator.view.*

class RecordValidatorVH(parent: ViewGroup, private val clickListener: ((SimpleValidator, Int) -> Unit)?) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_record_select_validator)) {
    init {

    }

    fun render(item: SimpleValidator, isChecked: Boolean) = with(itemView) {

        checkBox.isChecked = isChecked
        tv_name.text = item.name
        tv_title.text = item.title
        iv_icon.setImageUrl(item.imageUrl)

        root.setOnClickListener {
            clickListener?.invoke(item, adapterPosition)
        }

    }
}