package io.forus.me.views.wallet

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.R
import io.forus.me.entities.base.WalletItem
import io.forus.me.helpers.QrHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.web3.base.UpdateEvent
import rx.Subscriber

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class WalletListAdapter<T : WalletItem>(private val resourceLayout: Int, private val listener: ItemListener<T>) : RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {

    private var _items: List<T> = emptyList()
    var items: List<T>
        get() {
            return _items
        }
        set(newItems) {
            val removedItems = this.items.filterNot { newItems.contains(it) }
            for (item in removedItems) {
                listener.onItemDetach(item)
            }
            val addedItems = newItems.filterNot { items.contains(it) }
            _items = newItems
            for (item in addedItems) {
                listener.onItemAttach(item)
            }
        }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        if (holder.nameView != null) holder.nameView.text = items[position].name
        if (holder.qrView != null) {
            val onColor = ContextCompat.getColor(holder.itemView.context, R.color.black)
            val offColor = ContextCompat.getColor(holder.itemView.context, R.color.transparent)
            val size = holder.qrView.layoutParams.width
            val bitmap = QrHelper.getQrBitmap(items[position].address, size, onColor, offColor)
            holder.qrView.setImageBitmap(bitmap)
        }
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
        val qrView: ImageView? = view.findViewById(R.id.qrView)
        val nameView: TextView? = view.findViewById(R.id.name_view)
        val valueView: TextView? = view.findViewById(R.id.value_view)
    }

    interface ItemListener<in T: WalletItem> {
        fun onItemAttach(newItem:T)
        fun onItemDetach(removedItem:T)
        fun onItemSelect(selected:T)
        fun onItemUpdate(updateEvent: UpdateEvent, updatedItem:T)
    }

    open class UpdateSubscriber<T:WalletItem, E: UpdateEvent>(private val listener: ItemListener<T>, private val item: T) : Subscriber<E>() {
        override fun onError(e: Throwable?) {
            Log.e("TokenSubscriber", e?.message)
        }

        override fun onCompleted() {
        }

        override fun onNext(updateEvent: E?) {
            if (updateEvent != null) {
                ThreadHelper.dispense(ThreadHelper.TOKEN_THREAD).postTask(
                        Runnable {
                            listener.onItemUpdate(updateEvent, this.item)
                        })
            }
        }

    }
}