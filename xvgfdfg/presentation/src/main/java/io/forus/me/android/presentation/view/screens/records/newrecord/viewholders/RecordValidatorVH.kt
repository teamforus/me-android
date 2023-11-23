package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.databinding.ItemRecordSelectValidatorBinding


class RecordValidatorVH(private val binding: ItemRecordSelectValidatorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: SimpleValidator,
        isChecked: Boolean,
        clickListener: ((SimpleValidator, Int) -> Unit)?
    ) {
        binding.apply {
            checkBox.isChecked = isChecked
            tvName.text = item.name
            tvTitle.text = item.title
            ivIcon.setImageUrl(item.imageUrl)

            root.setOnClickListener {
                clickListener?.invoke(item, adapterPosition)
            }
        }
    }
}