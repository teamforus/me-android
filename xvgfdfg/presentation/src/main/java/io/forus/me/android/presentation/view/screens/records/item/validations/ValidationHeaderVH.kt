package io.forus.me.android.presentation.view.screens.records.item.validations

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.presentation.databinding.ItemValidatorListHeaderBinding


class ValidationHeaderVH(private val binding: ItemValidatorListHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: ValidationViewModel
    ) {
        binding.apply {
            tvHeader.text = item.sectionName
        }
    }
}