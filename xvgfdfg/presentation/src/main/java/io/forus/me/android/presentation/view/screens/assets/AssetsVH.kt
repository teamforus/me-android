package io.forus.me.android.presentation.view.screens.assets

import androidx.recyclerview.widget.RecyclerView
import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.presentation.databinding.ItemAssetsBinding


class AssetsVH(private val binding: ItemAssetsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: Asset
    ) {
        binding.apply {
            type.text = item.type.name
            name.text = item.name
            description.text = item.description
            logo.setImageUrl(item.logoUrl)
        }
    }
}