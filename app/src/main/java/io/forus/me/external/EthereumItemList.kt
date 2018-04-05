package io.forus.me.external

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.R
import io.forus.me.entities.base.EthereumItem

/**
 * Created by martijn.doornik on 07/03/2018.
 */
class EthereumItemList(val items: List<EthereumItem> = emptyList()) : RecyclerView.Adapter<EthereumItemList.EthereumItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EthereumItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.external_login_attribute_list, parent, false)
        return EthereumItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: EthereumItemViewHolder, position: Int) {
        holder.textView.text = items[position].name
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class EthereumItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.external_login_label)
    }
}