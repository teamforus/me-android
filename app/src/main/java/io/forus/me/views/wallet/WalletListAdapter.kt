package io.forus.me.views.wallet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.R
import io.forus.me.entities.base.WalletItem

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class WalletListAdapter<T : WalletItem>(private val resourceLayout: Int, private val listener:ItemSelectionListener<T>) : RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {

    var items: List<T> = emptyList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        if (holder.nameView != null) holder.nameView.text = items[position].name
        if (holder.qrView != null) holder.qrView.setImageBitmap(items[position].qrCode)
        if (holder.valueView != null) holder.valueView.text = items[position].amount
        holder.itemView.setOnClickListener({
            listener.onItemSelect(items[position])
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resourceLayout, parent,false)
        return WalletViewHolder(view)
    }

    class WalletViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val qrView: ImageView? = view.findViewById(R.id.qr_view)
        val nameView: TextView? = view.findViewById(R.id.name_view)
        val valueView: TextView? = view.findViewById(R.id.value_view)
    }

    interface ItemSelectionListener<in T: WalletItem> {
        fun onItemSelect(selected:T)
    }
}