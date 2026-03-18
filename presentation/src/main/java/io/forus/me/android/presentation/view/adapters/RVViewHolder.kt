package io.forus.me.android.presentation.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RVViewHolder<Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var clickListener: ((Item) -> Unit)? = null

    abstract fun render(item: Item)
}