package io.forus.me.android.presentation.view.screens.assets

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.forus.me.android.domain.models.assets.Asset
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.inflate
import kotlinx.android.synthetic.main.item_assets.view.*


class AssetsVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_assets)) {
    init {

    }

    fun render(item:  Asset) = with(itemView) {

        type.text = item.type.name
        name.text = item.name
        description.text = item.description
        logo.setImageUrl(item.logoUrl)

    }
}