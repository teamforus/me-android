package io.forus.me.android.presentation.view.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class RVViewHolder<Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var clickListener: ((Item) -> Unit)? = null

    abstract fun render(item: Item)
}