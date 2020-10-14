package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.presentation.R
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class ActionsAdapter(var items: ArrayList<ProductAction>, val callback: Callback, val context: Context) : RecyclerView.Adapter<ActionsAdapter.MainHolder>() {


    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTV = itemView.findViewById<TextView>(R.id.nameTV)
        private val priceTV = itemView.findViewById<TextView>(R.id.priceTV)
        private val icon = itemView.findViewById<ImageView>(R.id.icon)


        fun bind(item: ProductAction) {
            nameTV.text = item.name
            priceTV.text = if (item.priceUser!!.toDouble() == 0.0) {
                context.getString(R.string.free)
            } else {
                NumberFormat.getCurrencyInstance(Locale("nl", "NL"))
                        .format(item.priceUser!!.toDouble())
            }

            val url = item.photoURL
            if (url != null && url.isNotEmpty()) {
                Glide.with(context).load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(icon)
            }

            itemView.setOnClickListener {
                callback.onItemClicked(item)
            }

        }
    }


    interface Callback {
        fun onItemClicked(item: ProductAction)
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == items!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }


    fun clearAll() {

        items.clear()
        notifyDataSetChanged()
    }

    fun add(item: ProductAction) {
        items.add(item!!)
        notifyItemInserted(items!!.size - 1)
    }

    fun addAll(moveResults: List<ProductAction>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun getItem(position: Int): ProductAction {
        return items!![position]
    }

}
