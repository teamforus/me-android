package io.forus.me.android.presentation.view.screens.records.item.validations

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.presentation.databinding.ItemValidationListBinding


class ValidationVH(private val binding: ItemValidationListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: ValidationViewModel,
        clickListener: ((ValidationViewModel) -> Unit)?
    ) {
        binding.apply {
            tvName.text = item.name
            tvTitle.text = item.title
            root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }
}